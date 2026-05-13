package ma.emsi.elevate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

/*
 * - Spring Security configure ici toute la securite de l'application via une SecurityFilterChain declarative.
 * - En JEE classique, il faudrait creer et configurer manuellement de multiples Filtres de Servlet.
 * - Ici, Spring gere l'authentification sans etat (stateless) par JWT.
 */
@Configuration
@EnableWebSecurity
/**
 * Configuration principale de Spring Security.
 * Definit les regles d'acces, le filtre JWT et l'authentification.
 */
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Fournit le provider qui relie UserDetailsService et PasswordEncoder.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Cree un AuthenticationManager base sur le provider DAO.
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(authenticationProvider()));
    }

    /**
     * Enregistre le filtre JWT qui authentifie chaque requete.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * Configure la chaine de securite HTTP (CSRF, CORS, sessions, regles d'acces).
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(org.springframework.security.config.Customizer.withDefaults())
                // Pas de session cote serveur, le JWT contient toutes les infos
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/jobs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                // Ajoute notre filtre JWT avant le filtre d'authentification standard de Spring
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
