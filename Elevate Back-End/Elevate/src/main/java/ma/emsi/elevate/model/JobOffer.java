package ma.emsi.elevate.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entite Offre d'emploi.
 */
@Entity
@Table(name = "job_offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOffer {

@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(nullable = false)
    private String title;

@Column(columnDefinition = "TEXT", nullable = false)
    private String description;

@Column(nullable = false)
    private String location;

@Column(nullable = false)
    private String jobType;

@Column(nullable = false)
    private String category;

@Column(nullable = false)
    private Double salary;

@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User recruiter;

@CreationTimestamp
    private LocalDateTime createdAt;

@UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();
}

