package ma.emsi.elevate.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entité représentant une candidature soumise par un candidat à une offre d'emploi.
 *
 * <p>Une candidature lie un {@link User} ayant le rôle {@code CANDIDATE} à une {@link JobOffer}.
 * Elle contient les fichiers joints (CV, lettre de motivation) et évolue à travers
 * différents statuts définis par {@link ApplicationStatus}.</p>
 *
 * <p>La table associée en base de données est {@code applications}.</p>
 */
@Entity
@Table(name = "applications")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    /** Identifiant unique généré automatiquement. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Candidat ayant soumis cette candidature (relation Many-to-One vers {@link User}). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    /** Offre d'emploi ciblée par cette candidature (relation Many-to-One vers {@link JobOffer}). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_offer_id", nullable = false)
    private JobOffer jobOffer;

    /** Statut actuel de la candidature (ex. : PENDING, REVIEWING, ACCEPTED…). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    /** Chemin ou URL vers le fichier CV téléversé par le candidat. */
    private String cvUrl;

    /** Chemin ou URL vers la lettre de motivation téléversée par le candidat (optionnel). */
    private String coverLetterUrl;

    /** Date et heure de soumission de la candidature (remplie automatiquement). */
    @CreationTimestamp
    private LocalDateTime appliedAt;

    /** Date et heure de la dernière modification de la candidature (mise à jour automatiquement). */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

