package ma.emsi.elevate.service;

import ma.emsi.elevate.dto.request.JobOfferRequest;
import ma.emsi.elevate.dto.response.JobOfferResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface définissant le contrat du service de gestion des offres d'emploi.
 *
 * <p>Permet aux recruteurs de créer, modifier et supprimer leurs offres, et à tous les
 * utilisateurs (y compris non authentifiés) de consulter et rechercher des offres.</p>
 */
public interface JobOfferService {

    /**
     * Crée une nouvelle offre d'emploi pour le recruteur connecté.
     *
     * @param request        les données de l'offre (titre, description, localisation, etc.)
     * @param recruiterEmail e-mail du recruteur propriétaire de l'offre
     * @return le DTO {@link JobOfferResponse} représentant l'offre créée
     */
    JobOfferResponse createJobOffer(JobOfferRequest request, String recruiterEmail);

    /**
     * Met à jour une offre d'emploi existante.
     * Réservé au recruteur propriétaire de l'offre.
     *
     * @param id             identifiant de l'offre à mettre à jour
     * @param request        les nouvelles données de l'offre
     * @param recruiterEmail e-mail du recruteur connecté
     * @return le DTO {@link JobOfferResponse} mis à jour
     */
    JobOfferResponse updateJobOffer(Long id, JobOfferRequest request, String recruiterEmail);

    /**
     * Supprime une offre d'emploi.
     * Réservé au recruteur propriétaire de l'offre.
     *
     * @param id             identifiant de l'offre à supprimer
     * @param recruiterEmail e-mail du recruteur connecté
     */
    void deleteJobOffer(Long id, String recruiterEmail);

    /**
     * Récupère une offre d'emploi par son identifiant.
     *
     * @param id identifiant de l'offre
     * @return le DTO {@link JobOfferResponse}
     */
    JobOfferResponse getJobOfferById(Long id);

    /**
     * Recherche des offres d'emploi par mot-clé et/ou catégorie avec pagination.
     *
     * @param keyword  mot-clé à rechercher dans le titre et la description (peut être {@code null})
     * @param category catégorie à filtrer (peut être {@code null})
     * @param pageable paramètres de pagination et de tri
     * @return page de DTOs {@link JobOfferResponse}
     */
    Page<JobOfferResponse> searchJobOffers(String keyword, String category, Pageable pageable);

    /**
     * Récupère de façon paginée toutes les offres publiées par un recruteur.
     *
     * @param recruiterId identifiant du recruteur
     * @param pageable    paramètres de pagination et de tri
     * @return page de DTOs {@link JobOfferResponse}
     */
    Page<JobOfferResponse> getJobOffersByRecruiter(Long recruiterId, Pageable pageable);
}

