package ma.emsi.elevate.controller;

import ma.emsi.elevate.dto.request.LoginRequest;
import ma.emsi.elevate.dto.request.RegisterRequest;
import ma.emsi.elevate.dto.response.LoginResponse;
import ma.emsi.elevate.dto.response.UserResponse;
import ma.emsi.elevate.dto.response.ErrorResponse;
import ma.emsi.elevate.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controleur REST public pour l'authentification (inscription, connexion).
 * Endpoints accessibles sans token JWT.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

private final AuthService authService;

public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Inscription d'un nouvel utilisateur.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequest));
    }

    /**
     * Connexion et generation du token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
