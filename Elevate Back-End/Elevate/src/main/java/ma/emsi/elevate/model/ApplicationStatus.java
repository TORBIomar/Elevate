package ma.emsi.elevate.model;

/**
 * Énumération représentant les différents statuts possibles d'une candidature.
 *
 * <p>Le cycle de vie d'une candidature suit généralement cet ordre :</p>
 * <ol>
 *   <li>{@link #PENDING} : candidature soumise, en attente de traitement</li>
 *   <li>{@link #REVIEWING} : candidature en cours d'examen par le recruteur</li>
 *   <li>{@link #INTERVIEW_SCHEDULED} : un entretien a été planifié</li>
 *   <li>{@link #ACCEPTED} : candidature acceptée</li>
 *   <li>{@link #REJECTED} : candidature refusée</li>
 * </ol>
 */
public enum ApplicationStatus {

    /** Candidature soumise, en attente de traitement par le recruteur. */
    PENDING,

    /** Candidature en cours d'examen par le recruteur. */
    REVIEWING,

    /** Un entretien a été planifié pour cette candidature. */
    INTERVIEW_SCHEDULED,

    /** Candidature acceptée : le candidat est retenu. */
    ACCEPTED,

    /** Candidature refusée : le candidat n'est pas retenu. */
    REJECTED
}

