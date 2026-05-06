package ma.emsi.elevate.service;

import ma.emsi.elevate.dto.request.UpdateProfileRequest;
import ma.emsi.elevate.dto.response.UserResponse;
import ma.emsi.elevate.mapper.UserMapper;
import ma.emsi.elevate.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserProfileService(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public UserResponse getProfileByEmail(String email) {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

    public UserResponse getUserById(Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

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

