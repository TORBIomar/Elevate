package ma.emsi.elevate.service;

import lombok.RequiredArgsConstructor;
import ma.emsi.elevate.dto.response.ApplicationResponse;
import ma.emsi.elevate.model.Application;
import ma.emsi.elevate.model.ApplicationStatus;
import ma.emsi.elevate.model.JobOffer;
import ma.emsi.elevate.model.User;
import ma.emsi.elevate.repository.ApplicationRepository;
import ma.emsi.elevate.repository.JobOfferRepository;
import ma.emsi.elevate.repository.UserRepository;
import ma.emsi.elevate.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/*
 * - Ce service délègue toutes ses opérations CRUD à Spring Data JPA (via les Repositories injectés).
 * - En JEE classique, l'implémentation d'un service similaire nécessiterait la création manuelle de requêtes SQL brutes (JDBC) ou la manipulation fastidieuse de l'EntityManager.
 * - Il faudrait également gérer manuellement les objets Connection, les blocs try-catch-finally, et les transactions manuelles, là où Spring gère tout cela implicitement (ou via @Transactional).
 */
/**
 * Logique metier des candidatures (creation, listing, statut).
 */
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobOfferRepository jobOfferRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    private final String UPLOAD_DIR = "uploads/";

    /**
     * Cree une candidature et sauvegarde les fichiers.
     */
    @Override
    public ApplicationResponse applyForJob(Long jobId, String username, MultipartFile cv, MultipartFile coverLetter) {
        User candidate = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobOffer job = jobOfferRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        String cvUrl = saveFile(cv);
        String clUrl = coverLetter != null ? saveFile(coverLetter) : null;

        Application app = Application.builder()
                .candidate(candidate)
                .jobOffer(job)
                .status(ApplicationStatus.PENDING)
                .cvUrl(cvUrl)
                .coverLetterUrl(clUrl)
                .build();

        Application saved = applicationRepository.save(app);
        return mapToResponse(saved);
    }

    /**
     * Liste les candidatures du candidat connecte.
     */
    @Override
    public Page<ApplicationResponse> getMyApplications(String username, Pageable pageable) {
        User candidate = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return applicationRepository.findByCandidateId(candidate.getId(), pageable)
                .map(this::mapToResponse);
    }

    /**
     * Liste les candidatures recues pour une offre du recruteur.
     */
    @Override
    public Page<ApplicationResponse> getApplicationsForJob(Long jobId, String username, Pageable pageable) {
        User recruiter = userRepository.findByEmail(username).orElseThrow();
        JobOffer jobOffer = jobOfferRepository.findById(jobId).orElseThrow();

        if(!jobOffer.getRecruiter().getId().equals(recruiter.getId())) {
             throw new RuntimeException("Unauthorized");
        }

        return applicationRepository.findByJobOfferId(jobId, pageable).map(this::mapToResponse);
    }

    /**
     * Met a jour le statut d'une candidature et notifie le candidat.
     */
    @Override
    public ApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatus status, String username) {
        Application app = applicationRepository.findById(applicationId).orElseThrow();
        User recruiter = userRepository.findByEmail(username).orElseThrow();
        if(!app.getJobOffer().getRecruiter().getId().equals(recruiter.getId())) {
             throw new RuntimeException("Unauthorized");
        }
        app.setStatus(status);
        Application saved = applicationRepository.save(app);

        notificationService.createNotification(
                app.getCandidate().getId(),
                "Your application status for " + app.getJobOffer().getTitle() + " has been updated to: " + status
        );

        return mapToResponse(saved);
    }

    /**
     * Sauvegarde un fichier dans le dossier local d'upload.
     */
    private String saveFile(MultipartFile file) {
        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + filename);
            Files.write(path, file.getBytes());
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file");
        }
    }

    /**
     * Convertit l'entite Application en DTO de reponse.
     */
    private ApplicationResponse mapToResponse(Application app) {
        return ApplicationResponse.builder()
                .id(app.getId())
                .candidateId(app.getCandidate().getId())
                .candidateName(app.getCandidate().getFirstName() + " " + app.getCandidate().getLastName())
                .jobOfferId(app.getJobOffer().getId())
                .jobOfferTitle(app.getJobOffer().getTitle())
                .status(app.getStatus())
                .cvUrl(app.getCvUrl())
                .coverLetterUrl(app.getCoverLetterUrl())
                .appliedAt(app.getAppliedAt())
                .build();
    }

@Override
    public void deleteApplication(Long id, String username) {
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Allow deletion if user is the candidate OR if user is the recruiter who owns the job
        boolean isCandidate = app.getCandidate().getId().equals(user.getId());
        boolean isRecruiter = app.getJobOffer().getRecruiter().getId().equals(user.getId());

        if (!isCandidate && !isRecruiter) {
            throw new RuntimeException("Unauthorized to delete this application");
        }

        applicationRepository.delete(app);
    }
}
