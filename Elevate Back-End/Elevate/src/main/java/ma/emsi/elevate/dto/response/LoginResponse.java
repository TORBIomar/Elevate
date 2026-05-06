package ma.emsi.elevate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String type;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}


