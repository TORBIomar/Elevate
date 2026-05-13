package ma.emsi.elevate.repository;

import ma.emsi.elevate.model.Application;
import ma.emsi.elevate.model.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository des candidatures.
 */
public interface ApplicationRepository extends JpaRepository<Application, Long> {

Page<Application> findByCandidateId(Long candidateId, Pageable pageable);

Page<Application> findByJobOfferId(Long jobOfferId, Pageable pageable);

long countByJobOffer_Recruiter_Id(Long recruiterId);

long countByJobOffer_Recruiter_IdAndStatus(Long recruiterId, ApplicationStatus status);
}
