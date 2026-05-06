package ma.emsi.elevate.controller;

import ma.emsi.elevate.dto.UserResponse;
import ma.emsi.elevate.model.User;
import ma.emsi.elevate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserResponse userResponse = convertToUserResponse(user);
            return ResponseEntity.ok(userResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.ErrorResponse("Failed to fetch profile", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserResponse userResponse = convertToUserResponse(user);
            return ResponseEntity.ok(userResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.ErrorResponse("User not found", e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Authentication authentication,
                                          @RequestBody UpdateProfileRequest updateRequest) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            User updatedUser = userService.updateUserProfile(
                    user.getId(),
                    updateRequest.getFirstName(),
                    updateRequest.getLastName(),
                    updateRequest.getPhoneNumber(),
                    updateRequest.getProfilePictureUrl()
            );

            UserResponse userResponse = convertToUserResponse(updatedUser);
            return ResponseEntity.ok(userResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthController.ErrorResponse("Failed to update profile", e.getMessage()));
        }
    }

    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .profilePictureUrl(user.getProfilePictureUrl())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt().format(formatter))
                .updatedAt(user.getUpdatedAt().format(formatter))
                .build();
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class UpdateProfileRequest {
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String profilePictureUrl;
    }
}

