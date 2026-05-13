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

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

private final JobOfferRepository jobOfferRepository;

private final ApplicationRepository applicationRepository;

private final InterviewRepository interviewRepository;

private final UserRepository userRepository;

@GetMapping("/recruiter/stats")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<RecruiterStatsResponse> getRecruiterStats(Authentication authentication) {
        
        User recruiter = userRepository.findByEmail(authentication.getName()).orElseThrow();
        Long recruiterId = recruiter.getId();

RecruiterStatsResponse stats = RecruiterStatsResponse.builder()
                .totalJobsOffered(jobOfferRepository.countByRecruiter_Id(recruiterId))
                .totalApplicationsReceived(applicationRepository.countByJobOffer_Recruiter_Id(recruiterId))
                .totalPendingApplications(applicationRepository.countByJobOffer_Recruiter_IdAndStatus(recruiterId, ApplicationStatus.PENDING))
                .totalInterviewsScheduled(interviewRepository.countByApplication_JobOffer_Recruiter_Id(recruiterId))
                .build();

        return ResponseEntity.ok(stats);
    }
}

