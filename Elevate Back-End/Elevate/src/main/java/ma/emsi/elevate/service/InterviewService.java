package ma.emsi.elevate.service;

import ma.emsi.elevate.dto.request.InterviewRequest;
import ma.emsi.elevate.dto.response.InterviewResponse;
import ma.emsi.elevate.model.InterviewStatus;
import java.util.List;

public interface InterviewService {
    InterviewResponse scheduleInterview(InterviewRequest request, String username);
    List<InterviewResponse> getMyInterviews(String username);
    InterviewResponse updateInterviewStatus(Long interviewId, InterviewStatus status, String username);
}

