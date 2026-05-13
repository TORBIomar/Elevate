package ma.emsi.elevate.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entite Entretien lie a une candidature.
 */
@Entity
@Table(name = "interviews")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Interview {

@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

private LocalDateTime scheduledDate;

private String interviewLinkOrLocation;

@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status;

@CreationTimestamp
    private LocalDateTime createdAt;
}

