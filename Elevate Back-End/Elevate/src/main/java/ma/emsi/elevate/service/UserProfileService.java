package ma.emsi.elevate.service;

import ma.emsi.elevate.dto.request.UpdateProfileRequest;
import ma.emsi.elevate.dto.response.UserResponse;
import ma.emsi.elevate.mapper.UserMapper;
import ma.emsi.elevate.model.User;
import org.springframework.stereotype.Service;

/**
 * Service de lecture et mise a jour du profil utilisateur.
 */
@Service
public class UserProfileService {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserProfileService(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Recupere le profil a partir de l'email.
     */
    public UserResponse getProfileByEmail(String email) {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

    /**
     * Recupere un utilisateur par id.
     */
    public UserResponse getUserById(Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

    /**
     * Met a jour le profil du user via email.
     */
    public UserResponse updateProfileByEmail(String email, UpdateProfileRequest request) {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User updatedUser = userService.updateUserProfile(
                user.getId(),
                request.getFirstName(),
                request.getLastName(),
                request.getPhoneNumber(),
                request.getProfilePictureUrl()
        );

        return userMapper.toUserResponse(updatedUser);
    }
}

