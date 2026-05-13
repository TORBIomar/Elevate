
package ma.emsi.elevate.repository;

import ma.emsi.elevate.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Référentiel JPA pour la gestion des entretiens
 * ({@link ma.emsi.elevate.model.Interview}) en base de données.
 *
 * <p>
 * Fournit les opérations CRUD standard via {@link JpaRepository} ainsi que des
 * méthodes
 * personnalisées permettant de récupérer les entretiens par candidat ou par
 * recruteur.
 * </p>
 */
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    /**
     * Récupère tous les entretiens associés à un candidat donné.
     *
     * @param candidateId identifiant du candidat
     * @return liste des entretiens du candidat
     */
    List<Interview> findByApplication_Candidate_Id(Long candidateId);

    /**
     * Récupère tous les entretiens gérés par un recruteur donné (via ses offres
     * d'emploi).
     *
     * @param recruiterId identifiant du recruteur
     * @return liste des entretiens liés aux offres du recruteur
     */
    List<Interview> findByApplication_JobOffer_Recruiter_Id(Long recruiterId);

    /**
     * Compte le nombre total d'entretiens planifiés pour les offres d'un recruteur.
     *
     * @param recruiterId identifiant du recruteur
     * @return nombre d'entretiens associés au recruteur
     */
    long countByApplication_JobOffer_Recruiter_Id(Long recruiterId);
}
