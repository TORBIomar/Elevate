package ma.emsi.elevate.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * DTO représentant les statistiques agrégées du tableau de bord d'un recruteur.
 *
 * <p>Retourné par l'endpoint {@code GET /api/dashboard/recruiter/stats}.
 * Fournit une vue synthétique de l'activité du recruteur sur la plateforme.</p>
 */
@Data
@Builder
public class RecruiterStatsResponse {

    /** Nombre total d'offres d'emploi publiées par le recruteur. */
    private long totalJobsOffered;

    /** Nombre total de candidatures reçues pour toutes les offres du recruteur. */
    private long totalApplicationsReceived;

    /** Nombre de candidatures en attente de traitement (statut PENDING). */
    private long totalPendingApplications;

    /** Nombre total d'entretiens planifiés par le recruteur. */
    private long totalInterviewsScheduled;
}

