package ma.emsi.elevate.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InterviewRequest {
    @NotNull(message = "Application ID is required")
    private Long applicationId;
    
    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;
    
    private String interviewLinkOrLocation;
}

