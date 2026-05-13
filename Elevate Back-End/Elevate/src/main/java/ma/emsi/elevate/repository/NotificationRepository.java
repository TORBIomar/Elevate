package ma.emsi.elevate.repository;

import ma.emsi.elevate.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository des notifications.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
}

