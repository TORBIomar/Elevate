package ma.emsi.elevate.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/*
 * - Cette entité JPA utilise des annotations puissantes (@ManyToOne, @OneToMany) pour lier automatiquement les tables en base de données.
 * - En JEE classique avec JDBC, il faudrait écrire manuellement de longues requêtes SQL avec des clauses JOIN complexes pour récupérer les relations.
 * - JPA mappe directement les résultats SQL en objets Java (ORM) de manière transparente et maintient les clés étrangères sans code supplémentaire.
 */
/**
 * Entite Candidature liee a une offre et un candidat.
 */
@Entity
@Table(name = "applications")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_offer_id", nullable = false)
    private JobOffer jobOffer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    private String cvUrl;

    private String coverLetterUrl;

    @CreationTimestamp
    private LocalDateTime appliedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Interview> interviews;
}
