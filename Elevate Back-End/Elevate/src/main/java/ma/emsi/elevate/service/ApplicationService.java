package ma.emsi.elevate.service;

import ma.emsi.elevate.dto.response.ApplicationResponse;
import ma.emsi.elevate.model.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Interface définissant le contrat du service de gestion des candidatures.
 *
 * <p>Fournit les opérations permettant à un candidat de soumettre une candidature
 * et à un recruteur de consulter et mettre à jour les candidatures reçues.</p>
 */
public interface ApplicationService {

    /**
     * Soumet une candidature pour une offre d'emploi.
     *
     * @param jobId       identifiant de l'offre d'emploi ciblée
     * @param username    e-mail du candidat connecté
     * @param cv          fichier CV du candidat (obligatoire)
     * @param coverLetter lettre de motivation (optionnel)
     * @return le DTO {@link ApplicationResponse} représentant la candidature créée
     */
    ApplicationResponse applyForJob(Long jobId, String username, MultipartFile cv, MultipartFile coverLetter);

    /**
     * Récupère de façon paginée les candidatures d'un candidat.
     *
     * @param username nom d'utilisateur (e-mail) du candidat
     * @param pageable paramètres de pagination et de tri
     * @return page de DTOs {@link ApplicationResponse}
     */
    Page<ApplicationResponse> getMyApplications(String username, Pageable pageable);

    /**
     * Récupère de façon paginée les candidatures reçues pour une offre d'emploi donnée.
     * Réservé au recruteur propriétaire de l'offre.
     *
     * @param jobId    identifiant de l'offre d'emploi
     * @param username e-mail du recruteur connecté
     * @param pageable paramètres de pagination et de tri
     * @return page de DTOs {@link ApplicationResponse}
     */
    Page<ApplicationResponse> getApplicationsForJob(Long jobId, String username, Pageable pageable);

    /**
     * Met à jour le statut d'une candidature.
     * Réservé au recruteur propriétaire de l'offre associée.
     *
     * @param applicationId identifiant de la candidature
     * @param status        nouveau statut à appliquer
     * @param username      e-mail du recruteur connecté
     * @return le DTO {@link ApplicationResponse} mis à jour
     */
    ApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatus status, String username);
}

