package ma.emsi.elevate.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entite Notification utilisateur.
 */
@Entity
@Table(name = "notifications")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

private String message;

private boolean isRead;

@CreationTimestamp
    private LocalDateTime createdAt;
}

