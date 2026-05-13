package ma.emsi.elevate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) représentant la requête de connexion d'un utilisateur.
 *
 * <p>Contient les identifiants nécessaires à l'authentification : l'adresse e-mail
 * et le mot de passe. Ces données sont transmises dans le corps de la requête HTTP
 * à l'endpoint {@code POST /api/auth/login}.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    /** Adresse e-mail de l'utilisateur (identifiant de connexion). */
    private String email;

    /** Mot de passe en clair de l'utilisateur (sera comparé au hash BCrypt en base). */
    private String password;
}


