package ma.emsi.elevate.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
/**
 * Fournit l'encodeur de mots de passe utilise dans l'application.
 */
public class PasswordEncoderConfig {

    /**
     * Encoder BCrypt pour le hash des mots de passe.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
