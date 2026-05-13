package ma.emsi.elevate.service;

import ma.emsi.elevate.model.User;
import ma.emsi.elevate.model.UserRole;
import ma.emsi.elevate.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service gérant toutes les opérations relatives aux utilisateurs de la plateforme Elevate.
 *
 * <p>Ce service implémente {@link UserDetailsService} afin d'être utilisé par Spring Security
 * pour charger les détails de l'utilisateur lors de l'authentification.</p>
 *
 * <p>Fonctionnalités offertes :</p>
 * <ul>
 *   <li>Inscription d'un nouvel utilisateur avec validation de l'unicité de l'e-mail</li>
 *   <li>Recherche d'utilisateur par e-mail ou identifiant</li>
 *   <li>Mise à jour du profil et de la date de dernière connexion</li>
 *   <li>Activation et désactivation d'un compte utilisateur</li>
 * </ul>
 */
@Service
public class UserService implements UserDetailsService {

    /** Référentiel JPA pour l'accès aux données utilisateur. */
    private final UserRepository userRepository;

    /** Encodeur de mots de passe BCrypt pour le hachage sécurisé. */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructeur avec injection de dépendances.
     *
     * @param userRepository  référentiel d'accès aux utilisateurs
     * @param passwordEncoder encodeur de mots de passe
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Charge les détails d'un utilisateur depuis la base de données à partir de son e-mail.
     * Méthode requise par {@link UserDetailsService} pour l'intégration Spring Security.
     *
     * @param username l'adresse e-mail de l'utilisateur
     * @return les détails de l'utilisateur
     * @throws UsernameNotFoundException si aucun utilisateur n'est trouvé avec cet e-mail
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    /**
     * Inscrit un nouvel utilisateur sur la plateforme.
     * Vérifie que l'e-mail n'est pas déjà utilisé, hache le mot de passe, puis persiste l'utilisateur.
     *
     * @param email       adresse e-mail (identifiant unique)
     * @param password    mot de passe en clair (sera haché)
     * @param firstName   prénom
     * @param lastName    nom de famille
     * @param phoneNumber numéro de téléphone (optionnel)
     * @param roleStr     rôle sous forme de chaîne : "CANDIDATE" ou "RECRUITER"
     * @return l'entité {@link User} persistée
     * @throws RuntimeException si l'e-mail est déjà enregistré
     */
    @Transactional
    public User registerUser(String email, String password, String firstName, String lastName,
                             String phoneNumber, String roleStr) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        // Conversion de la chaîne de rôle en énumération UserRole
        UserRole role = UserRole.valueOf(roleStr.toUpperCase());

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .role(role)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    /**
     * Recherche un utilisateur par son adresse e-mail.
     *
     * @param email l'adresse e-mail de l'utilisateur
     * @return un {@link Optional} contenant l'utilisateur s'il existe
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur
     * @return un {@link Optional} contenant l'utilisateur s'il existe
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Met à jour la date et l'heure de la dernière connexion de l'utilisateur.
     *
     * @param user l'utilisateur qui vient de se connecter
     * @return l'utilisateur mis à jour et persisté
     */
    @Transactional
    public User updateLastLogin(User user) {
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    /**
     * Met à jour les informations du profil d'un utilisateur.
     * Seuls les champs non nuls dans les paramètres sont modifiés.
     *
     * @param userId            identifiant de l'utilisateur à mettre à jour
     * @param firstName         nouveau prénom (ou {@code null} pour ne pas modifier)
     * @param lastName          nouveau nom (ou {@code null} pour ne pas modifier)
     * @param phoneNumber       nouveau téléphone (ou {@code null} pour ne pas modifier)
     * @param profilePictureUrl nouvelle URL de photo de profil (ou {@code null} pour ne pas modifier)
     * @return l'utilisateur mis à jour et persisté
     * @throws RuntimeException si l'utilisateur n'est pas trouvé
     */
    @Transactional
    public User updateUserProfile(Long userId, String firstName, String lastName,
                                  String phoneNumber, String profilePictureUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (firstName != null) {
            user.setFirstName(firstName);
        }
        if (lastName != null) {
            user.setLastName(lastName);
        }
        if (phoneNumber != null) {
            user.setPhoneNumber(phoneNumber);
        }
        if (profilePictureUrl != null) {
            user.setProfilePictureUrl(profilePictureUrl);
        }

        return userRepository.save(user);
    }

    /**
     * Désactive le compte d'un utilisateur (le compte devient inactif et ne peut plus se connecter).
     *
     * @param userId identifiant de l'utilisateur à désactiver
     * @return l'utilisateur désactivé et persisté
     * @throws RuntimeException si l'utilisateur n'est pas trouvé
     */
    @Transactional
    public User deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(false);
        return userRepository.save(user);
    }

    /**
     * Réactive le compte d'un utilisateur précédemment désactivé.
     *
     * @param userId identifiant de l'utilisateur à réactiver
     * @return l'utilisateur réactivé et persisté
     * @throws RuntimeException si l'utilisateur n'est pas trouvé
     */
    @Transactional
    public User activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(true);
        return userRepository.save(user);
    }
}
