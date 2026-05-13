package ma.emsi.elevate.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entité représentant une offre d'emploi publiée par un recruteur sur la plateforme Elevate.
 *
 * <p>Une offre d'emploi est créée par un recruteur et peut recevoir des candidatures de la part
 * des utilisateurs ayant le rôle {@code CANDIDATE}.</p>
 *
 * <p>La table associée en base de données est {@code job_offers}.</p>
 */
@Entity
@Table(name = "job_offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOffer {

    /** Identifiant unique généré automatiquement. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Intitulé du poste proposé. */
    @Column(nullable = false)
    private String title;

    /** Description détaillée du poste et des missions. */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    /** Lieu de travail (ville, région ou "Télétravail"). */
    @Column(nullable = false)
    private String location;

    /**
     * Type de contrat proposé.
     * Valeurs possibles : {@code FULL_TIME}, {@code PART_TIME}, {@code CONTRACT}, {@code INTERNSHIP}.
     */
    @Column(nullable = false)
    private String jobType;

    /** Catégorie ou domaine d'activité de l'offre (ex. : Informatique, Finance, etc.). */
    @Column(nullable = false)
    private String category;

    /** Salaire proposé (en unité monétaire locale). */
    @Column(nullable = false)
    private Double salary;

    /** Recruteur propriétaire de l'offre d'emploi (relation Many-to-One vers {@link User}). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User recruiter;

    /** Date et heure de création de l'offre (remplie automatiquement par Hibernate). */
    @CreationTimestamp
    private LocalDateTime createdAt;

    /** Date et heure de la dernière modification de l'offre (mise à jour automatiquement par Hibernate). */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

