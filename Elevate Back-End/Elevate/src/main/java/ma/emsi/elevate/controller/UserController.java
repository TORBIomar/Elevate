package ma.emsi.elevate.controller;

import ma.emsi.elevate.dto.request.UpdateProfileRequest;
import ma.emsi.elevate.dto.response.ErrorResponse;
import ma.emsi.elevate.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST gérant les profils utilisateurs sur la plateforme Elevate.
 *
 * <p>Expose les endpoints suivants sous {@code /api/users} :</p>
 * <ul>
 *   <li>{@code GET /profile} : consulter son propre profil (utilisateur connecté)</li>
 *   <li>{@code GET /{id}} : consulter le profil d'un utilisateur par son identifiant</li>
 *   <li>{@code PUT /profile} : mettre à jour son profil (utilisateur connecté)</li>
 * </ul>
 *
 * <p>L'annotation {@code @CrossOrigin} autorise les requêtes depuis n'importe quelle origine
 * (à restreindre en production).</p>
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    /** Service de gestion des profils utilisateurs. */
    private final UserProfileService userProfileService;

    /**
     * Constructeur avec injection de dépendances.
     *
     * @param userProfileService le service de profil utilisateur
     */
    public UserController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /**
     * Récupère le profil de l'utilisateur actuellement connecté.
     *
     * @param authentication objet d'authentification Spring Security
     * @return HTTP 200 (OK) avec le DTO du profil, ou HTTP 400 en cas d'erreur
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        try {
            return ResponseEntity.ok(userProfileService.getProfileByEmail(authentication.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Failed to fetch profile", e.getMessage()));
        }
    }

    /**
     * Récupère le profil d'un utilisateur par son identifiant.
     *
     * @param id identifiant de l'utilisateur à consulter
     * @return HTTP 200 (OK) avec le DTO du profil, ou HTTP 400 si l'utilisateur n'est pas trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userProfileService.getUserById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("User not found", e.getMessage()));
        }
    }

    /**
     * Met à jour le profil de l'utilisateur connecté.
     * Seuls les champs fournis dans la requête sont modifiés (les champs nuls sont ignorés).
     *
     * @param authentication objet d'authentification Spring Security
     * @param updateRequest  les nouvelles données du profil (prénom, nom, téléphone, URL photo)
     * @return HTTP 200 (OK) avec le DTO du profil mis à jour, ou HTTP 400 en cas d'erreur
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Authentication authentication,
                                           @RequestBody UpdateProfileRequest updateRequest) {
        try {
            return ResponseEntity.ok(userProfileService.updateProfileByEmail(authentication.getName(), updateRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Failed to update profile", e.getMessage()));
        }
    }
}
