package ma.emsi.elevate.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO représentant la requête de planification d'un entretien.
 *
 * <p>Contient les informations nécessaires pour créer un entretien associé à une candidature.
 * Utilisé par l'endpoint {@code POST /api/interviews} (réservé au recruteur).</p>
 */
@Data
public class InterviewRequest {

    /** Identifiant de la candidature pour laquelle l'entretien est planifié (obligatoire). */
    @NotNull(message = "Application ID is required")
    private Long applicationId;

    /** Date et heure planifiées pour l'entretien (obligatoire). */
    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;

    /** Lien de visioconférence ou adresse physique du lieu de l'entretien (optionnel). */
    private String interviewLinkOrLocation;
}

