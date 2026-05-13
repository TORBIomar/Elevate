
package ma.emsi.elevate.repository;

import ma.emsi.elevate.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

List<Interview> findByApplication_Candidate_Id(Long candidateId);

List<Interview> findByApplication_JobOffer_Recruiter_Id(Long recruiterId);

long countByApplication_JobOffer_Recruiter_Id(Long recruiterId);
}
