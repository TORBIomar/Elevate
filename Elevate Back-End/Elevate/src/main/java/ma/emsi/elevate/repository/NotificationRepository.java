package ma.emsi.elevate.repository;

import ma.emsi.elevate.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Référentiel JPA pour la gestion des notifications ({@link ma.emsi.elevate.model.Notification}) en base de données.
 *
 * <p>Fournit les opérations CRUD standard via {@link JpaRepository} ainsi qu'une méthode
 * personnalisée pour récupérer les notifications d'un utilisateur triées par date.</p>
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Récupère toutes les notifications d'un utilisateur, triées par date de création décroissante
     * (les plus récentes en premier).
     *
     * @param userId identifiant de l'utilisateur
     * @return liste des notifications de l'utilisateur, triées par ordre antéchronologique
     */
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
}

