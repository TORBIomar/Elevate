package ma.emsi.elevate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant la requête de mise à jour du profil d'un utilisateur.
 *
 * <p>Tous les champs sont optionnels : seuls les champs non nuls fournis dans la requête
 * seront pris en compte lors de la mise à jour du profil. Utilisé par l'endpoint
 * {@code PUT /api/users/profile}.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {

    /** Nouveau prénom de l'utilisateur (optionnel). */
    private String firstName;

    /** Nouveau nom de famille de l'utilisateur (optionnel). */
    private String lastName;

    /** Nouveau numéro de téléphone (optionnel). */
    private String phoneNumber;

    /** Nouvelle URL de la photo de profil (optionnel). */
    private String profilePictureUrl;
}

