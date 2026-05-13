package ma.emsi.elevate.repository;

import ma.emsi.elevate.model.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
 * - L'interface Spring Data JPA permet de déclarer des requêtes de recherche et de filtrage via de simples signatures de méthodes ou des annotations @Query lisibles.
 * - En JEE classique, l'implémentation d'une recherche avec des critères dynamiques et de la pagination exigerait la concaténation complexe et risquée de chaînes SQL.
 * - Le paramètre Pageable injecté par Spring évite d'avoir à gérer manuellement les calculs d'offsets (LIMIT/OFFSET) SQL.
 */
/**
 * Repository des offres d'emploi.
 */
@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

    /**
     * Recherche par mot-cle et categorie.
     */
@Query("SELECT j FROM JobOffer j WHERE (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND (:category IS NULL OR j.category = :category)")
    Page<JobOffer> searchOffers(@Param("keyword") String keyword, @Param("category") String category, Pageable pageable);

    Page<JobOffer> findByRecruiterId(Long recruiterId, Pageable pageable);

long countByRecruiter_Id(Long recruiterId);
}
