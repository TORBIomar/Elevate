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
 * Service gérant la logique d'authentification et d'inscription.
 * Fait le lien entre le contrôleur d'authentification, le gestionnaire de tokens (JwtUtil) et la base de données (UserService).
 */
/**
 * Service d'authentification: inscription et login avec JWT.
 */
@Service
public class AuthService {

private final AuthenticationManager authenticationManager;

private final UserService userService;

private final UserMapper userMapper;

private final JwtUtil jwtUtil;

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
     * Traite l'inscription d'un nouvel utilisateur.
     * @param request Objet contenant les informations du formulaire (email, mot de passe, etc.)
     * @return Les données de l'utilisateur formatées pour la réponse API
     */
            /**
             * Inscription d'un utilisateur.
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
     * Traite la connexion d'un utilisateur existant.
     * @param request Objet contenant l'email et le mot de passe
     * @return Les informations de connexion, incluant le token JWT
     */
            /**
             * Authentifie l'utilisateur et retourne un token.
             */
            public LoginResponse login(LoginRequest request) {

        // 1. Vérification des identifiants via Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Si succès, récupération des détails de l'utilisateur
        User user = (User) authentication.getPrincipal();
        
        // 3. Génération du token JWT
        String token = jwtUtil.generateToken(user);
        
        // 4. Mise à jour de la date de dernière connexion
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

