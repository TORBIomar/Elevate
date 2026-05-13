package ma.emsi.elevate.repository;

import ma.emsi.elevate.model.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

@Query("SELECT j FROM JobOffer j WHERE (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND (:category IS NULL OR j.category = :category)")
    Page<JobOffer> searchOffers(@Param("keyword") String keyword, @Param("category") String category, Pageable pageable);

Page<JobOffer> findByRecruiterId(Long recruiterId, Pageable pageable);

long countByRecruiter_Id(Long recruiterId);
}
