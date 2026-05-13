package ma.emsi.elevate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobOfferRequest {

@NotBlank(message = "Title is mandatory")
    private String title;

@NotBlank(message = "Description is mandatory")
    private String description;

@NotBlank(message = "Location is mandatory")
    private String location;

@NotBlank(message = "Job type is mandatory")
    private String jobType;

@NotBlank(message = "Category is mandatory")
    private String category;

@NotNull(message = "Salary is mandatory")
    private Double salary;
}

