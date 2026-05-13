package ma.emsi.elevate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant une réponse d'erreur retournée par l'API.
 *
 * <p>Utilisé pour structurer les réponses HTTP d'erreur (4xx, 5xx) avec un message
 * convivial et des détails techniques sur la cause de l'erreur.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    /** Message d'erreur général décrivant le type d'erreur rencontrée. */
    private String message;

    /** Détails supplémentaires sur la cause de l'erreur (message d'exception, etc.). */
    private String details;
}

