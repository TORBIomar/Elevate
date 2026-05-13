package ma.emsi.elevate.controller;

import ma.emsi.elevate.dto.request.LoginRequest;
import ma.emsi.elevate.dto.request.RegisterRequest;
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
 * Contrôleur REST gérant les opérations d'authentification de la plateforme Elevate.
 *
 * <p>Expose les endpoints publics suivants (aucun token JWT requis) :</p>
 * <ul>
 *   <li>{@code POST /api/auth/register} : inscription d'un nouvel utilisateur</li>
 *   <li>{@code POST /api/auth/login} : connexion et obtention d'un token JWT</li>
 * </ul>
 *
 * <p>L'annotation {@code @CrossOrigin} autorise les requêtes depuis n'importe quelle origine
 * (à restreindre en production).</p>
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    /** Service d'authentification gérant l'inscription et la connexion. */
    private final AuthService authService;

    /**
     * Constructeur avec injection de dépendances.
     *
     * @param authService le service d'authentification
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Inscrit un nouvel utilisateur sur la plateforme.
     *
     * @param registerRequest les données d'inscription (e-mail, mot de passe, prénom, nom, rôle)
     * @return HTTP 201 (Created) avec le DTO utilisateur créé, ou HTTP 400 si l'inscription échoue
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Registration failed", e.getMessage()));
        }
    }

    /**
     * Authentifie un utilisateur et retourne un token JWT.
     *
     * @param loginRequest les identifiants de connexion (e-mail et mot de passe)
     * @return HTTP 200 (OK) avec le DTO {@link ma.emsi.elevate.dto.response.LoginResponse},
     *         ou HTTP 401 si les identifiants sont invalides
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(authService.login(loginRequest));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Authentication failed", "Invalid email or password"));
        }
    }
}
