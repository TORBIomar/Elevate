package ma.emsi.elevate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant la réponse retournée après une connexion réussie.
 *
 * <p>Contient le token JWT d'accès ainsi que les informations de base de l'utilisateur
 * connecté, permettant au client de stocker l'état de session côté front-end.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    /** Token JWT d'accès à inclure dans l'en-tête {@code Authorization: Bearer <token>}. */
    private String token;

    /** Type du token (toujours {@code "Bearer"} pour les tokens JWT). */
    private String type;

    /** Identifiant unique de l'utilisateur connecté. */
    private Long userId;

    /** Adresse e-mail de l'utilisateur connecté. */
    private String email;

    /** Prénom de l'utilisateur connecté. */
    private String firstName;

    /** Nom de famille de l'utilisateur connecté. */
    private String lastName;

    /** Rôle de l'utilisateur connecté (ex. : {@code "CANDIDATE"}, {@code "RECRUITER"}). */
    private String role;
}


