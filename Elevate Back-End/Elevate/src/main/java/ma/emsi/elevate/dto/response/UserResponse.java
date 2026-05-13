package ma.emsi.elevate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant la réponse contenant les informations publiques d'un utilisateur.
 *
 * <p>Retourné par les endpoints de gestion des profils utilisateurs. Ne contient
 * aucune information sensible (pas de mot de passe). Utilisé après l'inscription
 * et pour la consultation de profil.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    /** Identifiant unique de l'utilisateur. */
    private Long id;

    /** Adresse e-mail de l'utilisateur. */
    private String email;

    /** Prénom de l'utilisateur. */
    private String firstName;

    /** Nom de famille de l'utilisateur. */
    private String lastName;

    /** Numéro de téléphone de l'utilisateur (peut être nul). */
    private String phoneNumber;

    /** Rôle de l'utilisateur (ex. : {@code "CANDIDATE"}, {@code "RECRUITER"}, {@code "ADMIN"}). */
    private String role;

    /** URL de la photo de profil de l'utilisateur (peut être nul). */
    private String profilePictureUrl;

    /** Indique si le compte est actif ({@code true}) ou désactivé ({@code false}). */
    private Boolean isActive;

    /** Date et heure de création du compte (format : {@code yyyy-MM-dd HH:mm:ss}). */
    private String createdAt;

    /** Date et heure de la dernière modification du compte (format : {@code yyyy-MM-dd HH:mm:ss}). */
    private String updatedAt;
}


