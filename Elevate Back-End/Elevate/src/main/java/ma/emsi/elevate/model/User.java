package ma.emsi.elevate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Entité représentant un utilisateur de la plateforme Elevate.
 *
 * <p>Un utilisateur peut avoir l'un des rôles suivants : {@code CANDIDATE}, {@code RECRUITER} ou {@code ADMIN}.
 * Cette classe implémente {@link UserDetails} afin d'être intégrée nativement au mécanisme
 * d'authentification de Spring Security.</p>
 *
 * <p>La table associée en base de données est {@code users}.</p>
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    /** Identifiant unique généré automatiquement. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Adresse e-mail de l'utilisateur (unique, sert de nom d'utilisateur). */
    @Column(nullable = false, unique = true)
    private String email;

    /** Mot de passe chiffré (BCrypt) de l'utilisateur. */
    @Column(nullable = false)
    private String password;

    /** Prénom de l'utilisateur. */
    @Column(nullable = false)
    private String firstName;

    /** Nom de famille de l'utilisateur. */
    @Column(nullable = false)
    private String lastName;

    /** Numéro de téléphone de l'utilisateur (optionnel). */
    @Column(name = "phone_number")
    private String phoneNumber;

    /** Rôle de l'utilisateur sur la plateforme (CANDIDATE, RECRUITER ou ADMIN). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    /** URL de la photo de profil de l'utilisateur (optionnel). */
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    /** Indique si le compte est actif ({@code true}) ou désactivé ({@code false}). */
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    /** Date et heure de création du compte (défini automatiquement à la création). */
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    /** Date et heure de la dernière modification du compte. */
    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    /** Date et heure de la dernière connexion de l'utilisateur. */
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // -------------------------------------------------------------------------
    // Implémentation de UserDetails (Spring Security)
    // -------------------------------------------------------------------------

    /**
     * Retourne les autorités accordées à l'utilisateur.
     * Le rôle est préfixé par {@code ROLE_} conformément aux conventions Spring Security.
     *
     * @return liste contenant l'autorité de l'utilisateur
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * Retourne le nom d'utilisateur utilisé pour l'authentification.
     * Sur cette plateforme, il s'agit de l'adresse e-mail.
     *
     * @return l'adresse e-mail de l'utilisateur
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indique si le compte n'a pas expiré.
     *
     * @return {@code true} toujours (pas de gestion d'expiration de compte)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indique si le compte n'est pas verrouillé.
     * Un compte inactif est considéré comme verrouillé.
     *
     * @return {@code true} si le compte est actif
     */
    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    /**
     * Indique si les informations d'identification n'ont pas expiré.
     *
     * @return {@code true} toujours (pas de gestion d'expiration des identifiants)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indique si le compte est activé.
     *
     * @return {@code true} si le compte est actif
     */
    @Override
    public boolean isEnabled() {
        return isActive;
    }

    /**
     * Méthode de rappel JPA appelée avant la première persistance de l'entité.
     * Initialise les horodatages {@code createdAt} et {@code updatedAt}.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Méthode de rappel JPA appelée avant chaque mise à jour de l'entité.
     * Met à jour l'horodatage {@code updatedAt}.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

