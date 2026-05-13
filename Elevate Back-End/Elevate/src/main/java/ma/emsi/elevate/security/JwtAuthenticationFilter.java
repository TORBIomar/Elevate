package ma.emsi.elevate.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.emsi.elevate.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre d'authentification JWT exécuté une seule fois par requête HTTP.
 *
 * <p>Ce filtre intercepte chaque requête entrante et vérifie la présence d'un token JWT
 * dans l'en-tête {@code Authorization}. Si un token valide est trouvé, il charge les
 * informations de l'utilisateur et établit l'authentification dans le
 * {@link SecurityContextHolder} de Spring Security.</p>
 *
 * <p>Le flux de traitement est le suivant :</p>
 * <ol>
 *   <li>Extraction du token JWT depuis l'en-tête {@code Authorization: Bearer <token>}</li>
 *   <li>Extraction du nom d'utilisateur (e-mail) depuis le token</li>
 *   <li>Chargement des détails utilisateur via {@link UserDetailsService}</li>
 *   <li>Validation du token avec {@link JwtUtil#isTokenValid}</li>
 *   <li>Mise à jour du contexte de sécurité si le token est valide</li>
 * </ol>
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** Utilitaire JWT injecté pour la manipulation des tokens. */
    @Autowired
    private JwtUtil jwtUtil;

    /** Service de chargement des détails utilisateur injecté par Spring Security. */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Méthode principale du filtre, appelée une seule fois par requête HTTP.
     * Extrait et valide le token JWT, puis met à jour le contexte de sécurité si nécessaire.
     *
     * @param request     la requête HTTP entrante
     * @param response    la réponse HTTP
     * @param filterChain la chaîne de filtres à poursuivre
     * @throws ServletException en cas d'erreur de servlet
     * @throws IOException      en cas d'erreur d'entrée/sortie
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // Extraction du token JWT depuis l'en-tête Authorization
            String jwt = extractJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                // Extraction du nom d'utilisateur depuis le token
                String username = jwtUtil.extractUsername(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validation du token et mise à jour du contexte de sécurité
                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        // Passage à la suite de la chaîne de filtres
        filterChain.doFilter(request, response);
    }

    /**
     * Extrait le token JWT depuis l'en-tête {@code Authorization} de la requête HTTP.
     * L'en-tête doit être au format {@code Bearer <token>}.
     *
     * @param request la requête HTTP
     * @return le token JWT sans le préfixe "Bearer ", ou {@code null} si absent
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

