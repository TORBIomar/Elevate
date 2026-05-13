package ma.emsi.elevate.repository;

import ma.emsi.elevate.model.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Référentiel JPA pour la gestion des offres d'emploi ({@link ma.emsi.elevate.model.JobOffer}) en base de données.
 *
 * <p>Fournit les opérations CRUD standard via {@link JpaRepository} ainsi que des méthodes
 * personnalisées pour la recherche textuelle, le filtrage par recruteur et le comptage.</p>
 */
@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

    /**
     * Recherche des offres d'emploi selon un mot-clé (dans le titre ou la description)
     * et/ou une catégorie. Les paramètres {@code null} sont ignorés dans le filtre.
     *
     * @param keyword  mot-clé à rechercher dans le titre et la description (insensible à la casse)
     * @param category catégorie à filtrer (valeur exacte)
     * @param pageable paramètres de pagination et de tri
     * @return page d'offres correspondant aux critères
     */
    @Query("SELECT j FROM JobOffer j WHERE (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND (:category IS NULL OR j.category = :category)")
    Page<JobOffer> searchOffers(@Param("keyword") String keyword, @Param("category") String category, Pageable pageable);

    /**
     * Récupère de façon paginée toutes les offres d'emploi publiées par un recruteur.
     *
     * @param recruiterId identifiant du recruteur
     * @param pageable    paramètres de pagination et de tri
     * @return page des offres du recruteur
     */
    Page<JobOffer> findByRecruiterId(Long recruiterId, Pageable pageable);

    /**
     * Compte le nombre total d'offres publiées par un recruteur.
     *
     * @param recruiterId identifiant du recruteur
     * @return nombre d'offres publiées par ce recruteur
     */
    long countByRecruiter_Id(Long recruiterId);
}
