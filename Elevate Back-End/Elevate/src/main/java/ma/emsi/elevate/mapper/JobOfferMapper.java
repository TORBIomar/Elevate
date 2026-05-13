package ma.emsi.elevate.mapper;

import ma.emsi.elevate.dto.request.JobOfferRequest;
import ma.emsi.elevate.dto.response.JobOfferResponse;
import ma.emsi.elevate.model.JobOffer;
import org.springframework.stereotype.Component;

/**
 * Composant responsable de la conversion entre les DTOs et les entités {@link JobOffer}.
 *
 * <p>Ce mapper est utilisé par {@link ma.emsi.elevate.service.JobOfferServiceImpl} pour :</p>
 * <ul>
 *   <li>Convertir un DTO de requête ({@link JobOfferRequest}) en entité persistable</li>
 *   <li>Convertir une entité ({@link JobOffer}) en DTO de réponse ({@link JobOfferResponse})</li>
 * </ul>
 */
@Component
public class JobOfferMapper {

    /**
     * Convertit un DTO de requête {@link JobOfferRequest} en entité {@link JobOffer}.
     * L'identifiant et le recruteur ne sont pas définis ici (gérés par le service).
     *
     * @param request le DTO contenant les données de l'offre
     * @return l'entité {@link JobOffer} correspondante, ou {@code null} si le DTO est nul
     */
    public JobOffer toEntity(JobOfferRequest request) {
        if (request == null) {
            return null;
        }

        return JobOffer.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .jobType(request.getJobType())
                .category(request.getCategory())
                .salary(request.getSalary())
                .build();
    }

    /**
     * Convertit une entité {@link JobOffer} en DTO de réponse {@link JobOfferResponse}.
     *
     * @param entity l'entité offre d'emploi à convertir
     * @return le DTO de réponse, ou {@code null} si l'entité est nulle
     */
    public JobOfferResponse toResponse(JobOffer entity) {
        if (entity == null) {
            return null;
        }

        return JobOfferResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .location(entity.getLocation())
                .jobType(entity.getJobType())
                .category(entity.getCategory())
                .salary(entity.getSalary())
                // L'identifiant du recruteur est inclus uniquement si le recruteur est chargé
                .recruiterId(entity.getRecruiter() != null ? entity.getRecruiter().getId() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

