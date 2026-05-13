package ma.emsi.elevate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

/**
 * Configuration principale de la sécurité Spring Security pour l'application Elevate.
 *
 * <p>Cette classe configure :</p>
 * <ul>
 *   <li>L'encodeur de mots de passe BCrypt</li>
 *   <li>Le fournisseur d'authentification DAO (base de données)</li>
 *   <li>Le gestionnaire d'authentification</li>
 *   <li>Le filtre JWT ({@link JwtAuthenticationFilter})</li>
 *   <li>La chaîne de filtres de sécurité avec les règles d'accès aux routes</li>
 * </ul>
 *
 * <p>L'application utilise une politique de session <strong>sans état</strong> (STATELESS)
 * puisque l'authentification est entièrement gérée par les tokens JWT.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** Service chargé de récupérer les détails utilisateur depuis la base de données. */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Déclare l'encodeur de mots de passe BCrypt utilisé pour hacher et vérifier les mots de passe.
     *
     * @return instance de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure le fournisseur d'authentification DAO qui charge les utilisateurs depuis la base
     * de données et vérifie les mots de passe avec BCrypt.
     *
     * @return instance de {@link DaoAuthenticationProvider} configurée
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Déclare le gestionnaire d'authentification utilisé par {@link ma.emsi.elevate.service.AuthService}
     * pour authentifier les utilisateurs lors de la connexion.
     *
     * @return instance de {@link AuthenticationManager}
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(authenticationProvider()));
    }

    /**
     * Déclare le filtre JWT qui sera ajouté à la chaîne de filtres Spring Security.
     *
     * @return nouvelle instance de {@link JwtAuthenticationFilter}
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * Configure la chaîne de filtres de sécurité HTTP.
     *
     * <p>Règles définies :</p>
     * <ul>
     *   <li>CSRF désactivé (API REST stateless)</li>
     *   <li>Session stateless (pas de HttpSession)</li>
     *   <li>Routes publiques : POST {@code /api/auth/register}, POST {@code /api/auth/login},
     *       GET {@code /api/jobs/**}</li>
     *   <li>Routes authentifiées : toutes les autres requêtes</li>
     * </ul>
     * Le filtre JWT est ajouté avant le filtre d'authentification standard de Spring Security.
     *
     * @param http l'objet de configuration HTTP Security
     * @return la chaîne de filtres construite
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Désactivation de la protection CSRF (non nécessaire pour une API REST stateless)
                .csrf(csrf -> csrf.disable())
                // Politique de session sans état : aucune session HTTP n'est créée
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Routes publiques pour l'authentification
                        .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
                        // Consultation des offres d'emploi accessible à tous
                        .requestMatchers(HttpMethod.GET, "/api/jobs/**").permitAll()
                        // Profils utilisateur accessibles aux utilisateurs connectés
                        .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
                        // Toute autre requête nécessite une authentification
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                // Ajout du filtre JWT avant le filtre d'authentification standard
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

