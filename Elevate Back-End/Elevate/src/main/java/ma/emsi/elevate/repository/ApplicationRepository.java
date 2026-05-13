package ma.emsi.elevate.repository;

import ma.emsi.elevate.model.Application;
import ma.emsi.elevate.model.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Référentiel JPA pour la gestion des candidatures ({@link ma.emsi.elevate.model.Application}) en base de données.
 *
 * <p>Fournit les opérations CRUD standard via {@link JpaRepository} ainsi que des méthodes
 * personnalisées pour la consultation et le comptage des candidatures.</p>
 */
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    /**
     * Récupère de façon paginée toutes les candidatures d'un candidat donné.
     *
     * @param candidateId identifiant du candidat
     * @param pageable    paramètres de pagination et de tri
     * @return page de candidatures appartenant au candidat
     */
    Page<Application> findByCandidateId(Long candidateId, Pageable pageable);

    /**
     * Récupère de façon paginée toutes les candidatures liées à une offre d'emploi.
     *
     * @param jobOfferId identifiant de l'offre d'emploi
     * @param pageable   paramètres de pagination et de tri
     * @return page de candidatures pour l'offre donnée
     */
    Page<Application> findByJobOfferId(Long jobOfferId, Pageable pageable);

    /**
     * Compte le nombre total de candidatures reçues par un recruteur pour toutes ses offres.
     *
     * @param recruiterId identifiant du recruteur
     * @return nombre total de candidatures reçues
     */
    long countByJobOffer_Recruiter_Id(Long recruiterId);

    /**
     * Compte les candidatures d'un recruteur filtrées par statut.
     *
     * @param recruiterId identifiant du recruteur
     * @param status      statut de candidature à filtrer
     * @return nombre de candidatures correspondant au statut donné
     */
    long countByJobOffer_Recruiter_IdAndStatus(Long recruiterId, ApplicationStatus status);
}

