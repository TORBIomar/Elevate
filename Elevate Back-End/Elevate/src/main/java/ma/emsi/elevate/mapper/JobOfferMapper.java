package ma.emsi.elevate.mapper;

import ma.emsi.elevate.dto.request.JobOfferRequest;
import ma.emsi.elevate.dto.response.JobOfferResponse;
import ma.emsi.elevate.model.JobOffer;
import org.springframework.stereotype.Component;

@Component
public class JobOfferMapper {

    public JobOffer toEntity(JobOfferRequest request) {
        if (request == null) {
            return null;
        }

        return JobOffer.Builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .jobType(request.getJobType())
                .category(request.getCategory())
                .salary(request.getSalary())
                .build();
    }

    public JobOfferResponse toResponse(JobOffer entity) {
        if (entity == null) {
            return null;
        }

        return JobOfferResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .location(entity.getLocation())
                .jobType(entity.getJobType())
                .category(entity.getCategory())
                .salary(entity.getSalary())
                .recruiterId(entity.getRecruiter() != null ? entity.getRecruiter().getId() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

