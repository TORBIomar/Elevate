package ma.emsi.elevate.service;

import ma.emsi.elevate.dto.request.LoginRequest;
import ma.emsi.elevate.dto.request.RegisterRequest;
import ma.emsi.elevate.dto.response.LoginResponse;
import ma.emsi.elevate.dto.response.UserResponse;
import ma.emsi.elevate.mapper.UserMapper;
import ma.emsi.elevate.model.User;
import ma.emsi.elevate.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service gérant l'authentification des utilisateurs sur la plateforme Elevate.
 *
 * <p>Ce service orchestre les opérations d'inscription et de connexion en s'appuyant sur :</p>
 * <ul>
 *   <li>{@link UserService} pour la gestion des entités utilisateur</li>
 *   <li>{@link AuthenticationManager} de Spring Security pour la vérification des identifiants</li>
 *   <li>{@link JwtUtil} pour la génération des tokens JWT</li>
 *   <li>{@link UserMapper} pour la conversion entité → DTO</li>
 * </ul>
 */
@Service
public class AuthService {

    /** Gestionnaire d'authentification Spring Security. */
    private final AuthenticationManager authenticationManager;

    /** Service utilisateur pour l'inscription et la récupération des données. */
    private final UserService userService;

    /** Mapper pour convertir les entités User en DTOs de réponse. */
    private final UserMapper userMapper;

    /** Utilitaire JWT pour la génération des tokens d'accès. */
    private final JwtUtil jwtUtil;

    /**
     * Constructeur avec injection de dépendances.
     *
     * @param authenticationManager gestionnaire d'authentification Spring Security
     * @param userService           service de gestion des utilisateurs
     * @param userMapper            mapper entité vers DTO
     * @param jwtUtil               utilitaire JWT
     */
    public AuthService(AuthenticationManager authenticationManager,
                       UserService userService,
                       UserMapper userMapper,
                       JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Inscrit un nouvel utilisateur à partir des données de la requête d'inscription.
     *
     * @param request les données d'inscription (e-mail, mot de passe, prénom, nom, téléphone, rôle)
     * @return le DTO {@link UserResponse} représentant l'utilisateur créé
     * @throws RuntimeException si l'e-mail est déjà utilisé
     */
    public UserResponse register(RegisterRequest request) {
        User user = userService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getPhoneNumber(),
                request.getRole()
        );
        return userMapper.toUserResponse(user);
    }

    /**
     * Authentifie un utilisateur et retourne un token JWT ainsi que les informations de session.
     *
     * <p>Processus :</p>
     * <ol>
     *   <li>Vérification des identifiants via {@link AuthenticationManager}</li>
     *   <li>Génération d'un token JWT signé</li>
     *   <li>Mise à jour de la date de dernière connexion</li>
     *   <li>Retour du DTO {@link LoginResponse} avec le token et les informations utilisateur</li>
     * </ol>
     *
     * @param request les identifiants de connexion (e-mail et mot de passe)
     * @return le DTO {@link LoginResponse} contenant le token JWT et les informations utilisateur
     * @throws org.springframework.security.core.AuthenticationException si les identifiants sont invalides
     */
    public LoginResponse login(LoginRequest request) {
        // Vérification des identifiants par Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        // Génération du token JWT pour la session
        String token = jwtUtil.generateToken(user);
        // Mise à jour de la date de dernière connexion
        userService.updateLastLogin(user);

        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }
}

