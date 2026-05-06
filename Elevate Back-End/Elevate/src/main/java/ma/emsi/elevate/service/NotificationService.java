package ma.emsi.elevate.service;

import ma.emsi.elevate.dto.response.NotificationResponse;
import java.util.List;

public interface NotificationService {
    void createNotification(Long userId, String message);
    List<NotificationResponse> getMyNotifications(String username);
    void markAsRead(Long notificationId, String username);
}

