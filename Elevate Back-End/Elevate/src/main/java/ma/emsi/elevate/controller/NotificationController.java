package ma.emsi.elevate.controller;

import lombok.RequiredArgsConstructor;
import ma.emsi.elevate.dto.response.NotificationResponse;
import ma.emsi.elevate.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST gérant les notifications des utilisateurs sur la plateforme Elevate.
 *
 * <p>Expose les endpoints suivants sous {@code /api/notifications} :</p>
 * <ul>
 *   <li>{@code GET /} : récupérer toutes les notifications de l'utilisateur connecté</li>
 *   <li>{@code PATCH /{id}/read} : marquer une notification comme lue</li>
 * </ul>
 *
 * <p>Tous les endpoints nécessitent une authentification (token JWT valide).</p>
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    /** Service de gestion des notifications. */
    private final NotificationService notificationService;

    /**
     * Récupère toutes les notifications de l'utilisateur connecté, triées par date décroissante.
     *
     * @param authentication objet d'authentification Spring Security
     * @return HTTP 200 (OK) avec la liste des DTOs {@link NotificationResponse}
     */
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(Authentication authentication) {
        return ResponseEntity.ok(notificationService.getMyNotifications(authentication.getName()));
    }

    /**
     * Marque une notification comme lue.
     * Seul le propriétaire de la notification peut effectuer cette action.
     *
     * @param id             identifiant de la notification à marquer comme lue
     * @param authentication objet d'authentification Spring Security
     * @return HTTP 204 (No Content) si l'opération a réussi
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id, Authentication authentication) {
        notificationService.markAsRead(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}

