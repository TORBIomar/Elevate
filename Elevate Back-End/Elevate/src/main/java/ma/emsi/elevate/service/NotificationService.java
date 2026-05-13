package ma.emsi.elevate.service;

import ma.emsi.elevate.dto.response.NotificationResponse;
import java.util.List;

/**
 * Interface définissant le contrat du service de gestion des notifications.
 *
 * <p>Les notifications sont envoyées automatiquement aux utilisateurs lors de changements
 * d'état importants (mise à jour de candidature, planification d'entretien, etc.).</p>
 */
public interface NotificationService {

    /**
     * Crée et persiste une nouvelle notification pour un utilisateur donné.
     *
     * @param userId  identifiant de l'utilisateur destinataire
     * @param message contenu textuel du message de la notification
     */
    void createNotification(Long userId, String message);

    /**
     * Récupère toutes les notifications de l'utilisateur connecté,
     * triées par date de création décroissante.
     *
     * @param username e-mail de l'utilisateur connecté
     * @return liste des DTOs {@link NotificationResponse}
     */
    List<NotificationResponse> getMyNotifications(String username);

    /**
     * Marque une notification comme lue.
     * Seul le propriétaire de la notification peut effectuer cette opération.
     *
     * @param notificationId identifiant de la notification
     * @param username       e-mail de l'utilisateur connecté (propriétaire de la notification)
     */
    void markAsRead(Long notificationId, String username);
}

