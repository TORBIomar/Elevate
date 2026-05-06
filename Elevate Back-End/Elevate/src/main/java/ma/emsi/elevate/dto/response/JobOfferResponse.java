package ma.emsi.elevate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobOfferResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String jobType;
    private String category;
    private Double salary;
    private Long recruiterId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

