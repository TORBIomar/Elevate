package ma.emsi.elevate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant la requête d'inscription d'un nouvel utilisateur.
 *
 * <p>Contient toutes les informations nécessaires à la création d'un compte sur la plateforme.
 * Ces données sont transmises dans le corps de la requête HTTP à l'endpoint
 * {@code POST /api/auth/register}.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    /** Adresse e-mail unique de l'utilisateur (servira d'identifiant de connexion). */
    private String email;

    /** Mot de passe en clair (sera haché avec BCrypt avant persistance). */
    private String password;

    /** Prénom de l'utilisateur. */
    private String firstName;

    /** Nom de famille de l'utilisateur. */
    private String lastName;

    /** Numéro de téléphone de l'utilisateur (optionnel). */
    private String phoneNumber;

    /**
     * Rôle souhaité pour le compte : {@code "CANDIDATE"} ou {@code "RECRUITER"}.
     * La valeur est insensible à la casse lors du traitement.
     */
    private String role;
}


