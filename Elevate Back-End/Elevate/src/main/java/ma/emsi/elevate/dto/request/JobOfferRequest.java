package ma.emsi.elevate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant la requête de création ou de mise à jour d'une offre d'emploi.
 *
 * <p>Tous les champs sont obligatoires et validés par les contraintes de validation Bean.
 * Utilisé par les endpoints {@code POST /api/jobs} et {@code PUT /api/jobs/{id}}.</p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobOfferRequest {

    /** Intitulé du poste (obligatoire). */
    @NotBlank(message = "Title is mandatory")
    private String title;

    /** Description détaillée du poste et des missions (obligatoire). */
    @NotBlank(message = "Description is mandatory")
    private String description;

    /** Lieu de travail (obligatoire). */
    @NotBlank(message = "Location is mandatory")
    private String location;

    /**
     * Type de contrat (obligatoire).
     * Valeurs attendues : {@code FULL_TIME}, {@code PART_TIME}, {@code CONTRACT}, {@code INTERNSHIP}.
     */
    @NotBlank(message = "Job type is mandatory")
    private String jobType;

    /** Catégorie ou domaine d'activité (obligatoire). */
    @NotBlank(message = "Category is mandatory")
    private String category;

    /** Salaire proposé (obligatoire). */
    @NotNull(message = "Salary is mandatory")
    private Double salary;
}

