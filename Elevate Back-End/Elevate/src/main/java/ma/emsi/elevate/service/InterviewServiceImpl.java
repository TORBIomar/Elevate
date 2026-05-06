package ma.emsi.elevate.service;

import lombok.RequiredArgsConstructor;
import ma.emsi.elevate.dto.request.InterviewRequest;
import ma.emsi.elevate.dto.response.InterviewResponse;
import ma.emsi.elevate.model.Application;
import ma.emsi.elevate.model.ApplicationStatus;
import ma.emsi.elevate.model.Interview;
import ma.emsi.elevate.model.InterviewStatus;
import ma.emsi.elevate.model.User;
import ma.emsi.elevate.repository.ApplicationRepository;
import ma.emsi.elevate.repository.InterviewRepository;
import ma.emsi.elevate.repository.UserRepository;
import ma.emsi.elevate.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public InterviewResponse scheduleInterview(InterviewRequest request, String username) {
        User recruiter = userRepository.findByEmail(username).orElseThrow();
        Application app = applicationRepository.findById(request.getApplicationId()).orElseThrow();
        
        if(!app.getJobOffer().getRecruiter().getId().equals(recruiter.getId())) {
             throw new RuntimeException("Unauthorized");
        }

        Interview interview = Interview.builder()
                .application(app)
                .scheduledDate(request.getScheduledDate())
                .interviewLinkOrLocation(request.getInterviewLinkOrLocation())
                .status(InterviewStatus.SCHEDULED)
                .build();
                
        app.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        applicationRepository.save(app);

        notificationService.createNotification(
                app.getCandidate().getId(),
                "Good news! An interview has been scheduled for your application to " + app.getJobOffer().getTitle() + " on " + request.getScheduledDate()
        );

        return mapToResponse(interviewRepository.save(interview));
    }

    @Override
    public List<InterviewResponse> getMyInterviews(String username) {
        User user = userRepository.findByEmail(username).orElseThrow();
        if ("CANDIDATE".equals(user.getRole().name())) {
            return interviewRepository.findByApplication_Candidate_Id(user.getId())
                    .stream().map(this::mapToResponse).collect(Collectors.toList());
        } else {
            return interviewRepository.findByApplication_JobOffer_Recruiter_Id(user.getId())
                    .stream().map(this::mapToResponse).collect(Collectors.toList());
        }
    }

    @Override
    public InterviewResponse updateInterviewStatus(Long interviewId, InterviewStatus status, String username) {
        Interview interview = interviewRepository.findById(interviewId).orElseThrow();
        interview.setStatus(status);

        notificationService.createNotification(
                interview.getApplication().getCandidate().getId(),
                "Your interview status for " + interview.getApplication().getJobOffer().getTitle() + " has been updated to: " + status
        );

        return mapToResponse(interviewRepository.save(interview));
    }

    private InterviewResponse mapToResponse(Interview i) {
        return InterviewResponse.builder()
                .id(i.getId())
                .applicationId(i.getApplication().getId())
                .candidateName(i.getApplication().getCandidate().getFirstName() + " " + i.getApplication().getCandidate().getLastName())
                .jobTitle(i.getApplication().getJobOffer().getTitle())
                .scheduledDate(i.getScheduledDate())
                .interviewLinkOrLocation(i.getInterviewLinkOrLocation())
                .status(i.getStatus())
                .build();
    }
}
