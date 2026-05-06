package ma.emsi.elevate.controller;

import ma.emsi.elevate.dto.LoginRequest;
import ma.emsi.elevate.dto.LoginResponse;
import ma.emsi.elevate.dto.RegisterRequest;
import ma.emsi.elevate.dto.UserResponse;
import ma.emsi.elevate.model.User;
import ma.emsi.elevate.service.UserService;
import ma.emsi.elevate.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.registerUser(
                    registerRequest.getEmail(),
                    registerRequest.getPassword(),
                    registerRequest.getFirstName(),
                    registerRequest.getLastName(),
                    registerRequest.getPhoneNumber(),
                    registerRequest.getRole()
            );

            UserResponse userResponse = convertToUserResponse(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Registration failed", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();
            String token = jwtUtil.generateToken(user);

            // Update last login
            userService.updateLastLogin(user);

            LoginResponse loginResponse = LoginResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .userId(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getRole().name())
                    .build();

            return ResponseEntity.ok(loginResponse);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Authentication failed", "Invalid email or password"));
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String message;
        private String details;
    }

    // Adding lombok annotation import placeholder
}

