package ma.emsi.elevate.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entité représentant une notification envoyée à un utilisateur de la plateforme.
 *
 * <p>Les notifications sont créées automatiquement lors de changements d'état importants,
 * par exemple lorsqu'une candidature est mise à jour ou qu'un entretien est planifié.
 * L'utilisateur peut marquer une notification comme lue via l'API dédiée.</p>
 *
 * <p>La table associée en base de données est {@code notifications}.</p>
 */
@Entity
@Table(name = "notifications")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    /** Identifiant unique généré automatiquement. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Utilisateur destinataire de la notification (relation Many-to-One vers {@link User}). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Contenu textuel du message de la notification. */
    private String message;

    /** Indique si la notification a été lue par l'utilisateur ({@code true}) ou non ({@code false}). */
    private boolean isRead;

    /** Date et heure de création de la notification (remplie automatiquement par Hibernate). */
    @CreationTimestamp
    private LocalDateTime createdAt;
}

