package ma.emsi.elevate.controller;

import lombok.RequiredArgsConstructor;
import ma.emsi.elevate.dto.response.RecruiterStatsResponse;
import ma.emsi.elevate.model.ApplicationStatus;
import ma.emsi.elevate.model.User;
import ma.emsi.elevate.repository.ApplicationRepository;
import ma.emsi.elevate.repository.InterviewRepository;
import ma.emsi.elevate.repository.JobOfferRepository;
import ma.emsi.elevate.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST fournissant les statistiques du tableau de bord recruteur.
 *
 * <p>Expose l'endpoint suivant sous {@code /api/dashboard} :</p>
 * <ul>
 *   <li>{@code GET /recruiter/stats} : statistiques agrégées pour le recruteur connecté
 *       (nombre d'offres, candidatures, candidatures en attente, entretiens planifiés)</li>
 * </ul>
 *
 * <p>Accès réservé aux utilisateurs ayant le rôle {@code RECRUITER}.</p>
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    /** Référentiel des offres d'emploi pour le comptage. */
    private final JobOfferRepository jobOfferRepository;

    /** Référentiel des candidatures pour le comptage et le filtrage par statut. */
    private final ApplicationRepository applicationRepository;

    /** Référentiel des entretiens pour le comptage. */
    private final InterviewRepository interviewRepository;

    /** Référentiel des utilisateurs pour récupérer le recruteur connecté. */
    private final UserRepository userRepository;

    /**
     * Retourne les statistiques agrégées du tableau de bord pour le recruteur connecté.
     *
     * <p>Les statistiques incluent :</p>
     * <ul>
     *   <li>Nombre total d'offres publiées</li>
     *   <li>Nombre total de candidatures reçues</li>
     *   <li>Nombre de candidatures en attente (statut PENDING)</li>
     *   <li>Nombre total d'entretiens planifiés</li>
     * </ul>
     *
     * @param authentication objet d'authentification Spring Security
     * @return HTTP 200 (OK) avec le DTO {@link RecruiterStatsResponse}
     */
    @GetMapping("/recruiter/stats")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<RecruiterStatsResponse> getRecruiterStats(Authentication authentication) {
        // Récupération du recruteur connecté depuis la base de données
        User recruiter = userRepository.findByEmail(authentication.getName()).orElseThrow();
        Long recruiterId = recruiter.getId();

        // Agrégation des statistiques depuis les référentiels
        RecruiterStatsResponse stats = RecruiterStatsResponse.builder()
                .totalJobsOffered(jobOfferRepository.countByRecruiter_Id(recruiterId))
                .totalApplicationsReceived(applicationRepository.countByJobOffer_Recruiter_Id(recruiterId))
                .totalPendingApplications(applicationRepository.countByJobOffer_Recruiter_IdAndStatus(recruiterId, ApplicationStatus.PENDING))
                .totalInterviewsScheduled(interviewRepository.countByApplication_JobOffer_Recruiter_Id(recruiterId))
                .build();

        return ResponseEntity.ok(stats);
    }
}

