package ma.emsi.elevate.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Composant utilitaire pour la gestion des tokens JWT (JSON Web Token).
 *
 * <p>Cette classe fournit les fonctionnalités suivantes :</p>
 * <ul>
 *   <li>Génération d'un token JWT signé pour un utilisateur authentifié</li>
 *   <li>Extraction du nom d'utilisateur (e-mail) depuis un token</li>
 *   <li>Validation d'un token (signature et expiration)</li>
 * </ul>
 *
 * <p>Les paramètres de configuration ({@code jwt.secret} et {@code jwt.expiration}) sont
 * injectés depuis le fichier {@code application.properties}.</p>
 */
@Component
public class JwtUtil {

    /**
     * Clé secrète utilisée pour signer et vérifier les tokens JWT.
     * Doit être changée en production et avoir au moins 32 caractères.
     */
    @Value("${jwt.secret:elevate-secret-key-that-should-be-changed-in-production-environment-with-at-least-32-characters}")
    private String jwtSecret;

    /** Durée de validité du token JWT en millisecondes (par défaut : 86400000 = 24 heures). */
    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    /**
     * Construit et retourne la clé secrète HMAC-SHA à partir de la chaîne de configuration.
     *
     * @return clé secrète de signature
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Génère un token JWT pour l'utilisateur authentifié.
     * Le rôle de l'utilisateur est inclus comme revendication ({@code claim}) dans le token.
     *
     * @param userDetails les détails de l'utilisateur authentifié
     * @return le token JWT signé sous forme de chaîne de caractères
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("USER"));
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Construit le token JWT avec les revendications, le sujet, les dates d'émission et d'expiration,
     * puis le signe avec l'algorithme HS256.
     *
     * @param claims  les revendications additionnelles à inclure dans le payload
     * @param subject le sujet du token (e-mail de l'utilisateur)
     * @return le token JWT compact signé
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrait le nom d'utilisateur (sujet) depuis un token JWT.
     *
     * @param token le token JWT
     * @return le nom d'utilisateur (e-mail) encodé dans le token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Valide un token JWT en vérifiant que le nom d'utilisateur correspond
     * et que le token n'est pas expiré.
     *
     * @param token       le token JWT à valider
     * @param userDetails les détails de l'utilisateur à comparer
     * @return {@code true} si le token est valide pour cet utilisateur
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Vérifie si un token JWT est expiré.
     *
     * @param token le token JWT à vérifier
     * @return {@code true} si la date d'expiration est passée
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Analyse et extrait toutes les revendications (payload) d'un token JWT signé.
     *
     * @param token le token JWT à analyser
     * @return les revendications contenues dans le token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

