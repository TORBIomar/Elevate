package ma.emsi.elevate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {

private String firstName;

private String lastName;

private String phoneNumber;

private String profilePictureUrl;
}

