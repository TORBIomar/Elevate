package ma.emsi.elevate.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entité représentant un entretien planifié dans le cadre d'une candidature.
 *
 * <p>Un entretien est créé par un recruteur pour une {@link Application} dont le statut
 * passe alors à {@code INTERVIEW_SCHEDULED}. Le candidat reçoit une notification automatique.</p>
 *
 * <p>La table associée en base de données est {@code interviews}.</p>
 */
@Entity
@Table(name = "interviews")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Interview {

    /** Identifiant unique généré automatiquement. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Candidature associée à cet entretien (relation Many-to-One vers {@link Application}). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    /** Date et heure planifiées pour l'entretien. */
    private LocalDateTime scheduledDate;

    /** Lien de visioconférence ou lieu physique de l'entretien. */
    private String interviewLinkOrLocation;

    /** Statut actuel de l'entretien (SCHEDULED, COMPLETED ou CANCELED). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status;

    /** Date et heure de création de l'entretien (remplie automatiquement par Hibernate). */
    @CreationTimestamp
    private LocalDateTime createdAt;
}

