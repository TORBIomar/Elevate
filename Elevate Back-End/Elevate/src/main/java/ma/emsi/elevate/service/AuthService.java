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

public LoginResponse login(LoginRequest request) {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        
        String token = jwtUtil.generateToken(user);
        
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

