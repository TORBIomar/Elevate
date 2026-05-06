package ma.emsi.elevate.dto.response;

import lombok.Builder;
import lombok.Data;
import ma.emsi.elevate.model.ApplicationStatus;
import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationResponse {
    private Long id;
    private Long candidateId;
    private String candidateName;
    private Long jobOfferId;
    private String jobOfferTitle;
    private ApplicationStatus status;
    private String cvUrl;
    private String coverLetterUrl;
    private LocalDateTime appliedAt;
}

