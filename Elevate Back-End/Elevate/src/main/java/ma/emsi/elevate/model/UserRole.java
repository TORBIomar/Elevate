package ma.emsi.elevate.model;

/**
 * Énumération représentant les rôles disponibles pour les utilisateurs de la plateforme Elevate.
 *
 * <p>Chaque rôle détermine les permissions et les fonctionnalités accessibles à l'utilisateur :</p>
 * <ul>
 *   <li>{@link #CANDIDATE} : peut consulter et postuler à des offres d'emploi</li>
 *   <li>{@link #RECRUITER} : peut créer et gérer des offres d'emploi, consulter les candidatures</li>
 *   <li>{@link #ADMIN} : dispose des droits d'administration complets</li>
 * </ul>
 */
public enum UserRole {

    /** Rôle pour les candidats à la recherche d'emploi. */
    CANDIDATE("Candidate"),

    /** Rôle pour les recruteurs qui publient des offres d'emploi. */
    RECRUITER("Recruiter"),

    /** Rôle administrateur avec accès complet à la plateforme. */
    ADMIN("Admin");

    /** Nom d'affichage convivial du rôle. */
    private final String displayName;

    /**
     * Constructeur de l'énumération avec le nom d'affichage.
     *
     * @param displayName le nom lisible par l'utilisateur
     */
    UserRole(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Retourne le nom d'affichage du rôle.
     *
     * @return le nom lisible du rôle
     */
    public String getDisplayName() {
        return displayName;
    }
}

