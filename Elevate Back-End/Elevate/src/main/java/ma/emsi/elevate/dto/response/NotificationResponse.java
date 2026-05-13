package ma.emsi.elevate.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO représentant la réponse contenant les informations d'une notification.
 *
 * <p>Retourné par l'endpoint {@code GET /api/notifications}. Fournit les données
 * de la notification sans exposer l'entité JPA directement.</p>
 */
@Data
@Builder
public class NotificationResponse {

    /** Identifiant unique de la notification. */
    private Long id;

    /** Contenu textuel du message de la notification. */
    private String message;

    /** Indique si la notification a été lue ({@code true}) ou non ({@code false}). */
    private boolean isRead;

    /** Date et heure de création de la notification. */
    private LocalDateTime createdAt;
}

