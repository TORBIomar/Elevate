package ma.emsi.elevate.dto.response;

import lombok.Builder;
import lombok.Data;
import ma.emsi.elevate.model.ApplicationStatus;
import java.time.LocalDateTime;

/**
 * DTO représentant la réponse contenant les informations d'une candidature.
 *
 * <p>Retourné par les endpoints de gestion des candidatures. Contient les données
 * essentielles d'une candidature sans exposer les entités JPA directement.</p>
 */
@Data
@Builder
public class ApplicationResponse {

    /** Identifiant unique de la candidature. */
    private Long id;

    /** Identifiant du candidat ayant soumis la candidature. */
    private Long candidateId;

    /** Nom complet du candidat (prénom + nom). */
    private String candidateName;

    /** Identifiant de l'offre d'emploi ciblée. */
    private Long jobOfferId;

    /** Intitulé de l'offre d'emploi ciblée. */
    private String jobOfferTitle;

    /** Statut actuel de la candidature. */
    private ApplicationStatus status;

    /** URL ou chemin vers le fichier CV du candidat. */
    private String cvUrl;

    /** URL ou chemin vers la lettre de motivation du candidat (peut être nul). */
    private String coverLetterUrl;

    /** Date et heure de soumission de la candidature. */
    private LocalDateTime appliedAt;
}

