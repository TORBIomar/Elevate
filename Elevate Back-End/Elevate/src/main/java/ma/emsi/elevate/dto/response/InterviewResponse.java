package ma.emsi.elevate.dto.response;

import lombok.Builder;
import lombok.Data;
import ma.emsi.elevate.model.InterviewStatus;
import java.time.LocalDateTime;

/**
 * DTO représentant la réponse contenant les informations d'un entretien.
 *
 * <p>Retourné par les endpoints de gestion des entretiens. Fournit les détails
 * de l'entretien sans exposer les entités JPA directement.</p>
 */
@Data
@Builder
public class InterviewResponse {

    /** Identifiant unique de l'entretien. */
    private Long id;

    /** Identifiant de la candidature associée à cet entretien. */
    private Long applicationId;

    /** Nom complet du candidat concerné par cet entretien. */
    private String candidateName;

    /** Intitulé du poste de l'offre d'emploi associée. */
    private String jobTitle;

    /** Date et heure planifiées pour l'entretien. */
    private LocalDateTime scheduledDate;

    /** Lien de visioconférence ou adresse physique de l'entretien. */
    private String interviewLinkOrLocation;

    /** Statut actuel de l'entretien (SCHEDULED, COMPLETED ou CANCELED). */
    private InterviewStatus status;
}

