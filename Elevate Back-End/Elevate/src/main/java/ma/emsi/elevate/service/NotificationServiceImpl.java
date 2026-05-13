package ma.emsi.elevate.service;

import lombok.RequiredArgsConstructor;
import ma.emsi.elevate.dto.response.NotificationResponse;
import ma.emsi.elevate.model.Notification;
import ma.emsi.elevate.model.User;
import ma.emsi.elevate.repository.NotificationRepository;
import ma.emsi.elevate.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Logique metier des notifications utilisateur.
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    /**
     * Cree une notification pour un utilisateur.
     */
    @Override
    public void createNotification(Long userId, String message) {
        User user = userRepository.findById(userId).orElseThrow();
        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }

    /**
     * Retourne les notifications du user connecte.
     */
    @Override
    public List<NotificationResponse> getMyNotifications(String username) {
        User user = userRepository.findByEmail(username).orElseThrow();
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Marque une notification comme lue si elle appartient au user.
     */
    @Override
    public void markAsRead(Long notificationId, String username) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow();
        User user = userRepository.findByEmail(username).orElseThrow();

        if (notification.getUser().getId().equals(user.getId())) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    /**
     * Convertit l'entite Notification en DTO de reponse.
     */
    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}

