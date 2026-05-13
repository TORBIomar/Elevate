package ma.emsi.elevate.dto.response;

import lombok.Builder;
import lombok.Data;
import ma.emsi.elevate.model.InterviewStatus;
import java.time.LocalDateTime;

@Data
@Builder
public class InterviewResponse {

private Long id;

private Long applicationId;

private String candidateName;

private String jobTitle;

private LocalDateTime scheduledDate;

private String interviewLinkOrLocation;

private InterviewStatus status;
}

