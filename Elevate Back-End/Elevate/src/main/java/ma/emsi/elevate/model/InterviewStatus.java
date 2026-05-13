package ma.emsi.elevate.model;

/**
 * Énumération représentant les statuts possibles d'un entretien.
 *
 * <p>Un entretien créé par un recruteur passe par les états suivants :</p>
 * <ul>
 *   <li>{@link #SCHEDULED} : l'entretien est planifié et confirmé</li>
 *   <li>{@link #COMPLETED} : l'entretien a eu lieu</li>
 *   <li>{@link #CANCELED} : l'entretien a été annulé</li>
 * </ul>
 */
public enum InterviewStatus {

    /** L'entretien est planifié et en attente de réalisation. */
    SCHEDULED,

    /** L'entretien a été réalisé avec succès. */
    COMPLETED,

    /** L'entretien a été annulé. */
    CANCELED
}

