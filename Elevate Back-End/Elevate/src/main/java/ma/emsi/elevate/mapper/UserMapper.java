package ma.emsi.elevate.mapper;

import ma.emsi.elevate.dto.response.UserResponse;
import ma.emsi.elevate.model.User;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * Composant responsable de la conversion des entités {@link User} en DTOs de réponse {@link UserResponse}.
 *
 * <p>Ce mapper est utilisé pour exposer les données utilisateur à l'API REST sans inclure
 * les informations sensibles (comme le mot de passe haché).</p>
 */
@Component
public class UserMapper {

    /** Format de date utilisé pour la sérialisation des horodatages dans les réponses API. */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Convertit une entité {@link User} en DTO {@link UserResponse}.
     *
     * @param user l'entité utilisateur à convertir
     * @return le DTO contenant les informations publiques de l'utilisateur
     */
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .profilePictureUrl(user.getProfilePictureUrl())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt().format(FORMATTER))
                .updatedAt(user.getUpdatedAt().format(FORMATTER))
                .build();
    }
}

