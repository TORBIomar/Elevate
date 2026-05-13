package ma.emsi.elevate.controller;

import lombok.RequiredArgsConstructor;
import ma.emsi.elevate.dto.response.NotificationResponse;
import ma.emsi.elevate.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controleur REST des notifications utilisateur.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

private final NotificationService notificationService;

    /**
     * Retourne les notifications de l'utilisateur connecte.
     */
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(Authentication authentication) {
        return ResponseEntity.ok(notificationService.getMyNotifications(authentication.getName()));
    }

    /**
     * Marque une notification comme lue.
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id, Authentication authentication) {
        notificationService.markAsRead(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}

