package ma.emsi.elevate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.emsi.elevate.dto.request.InterviewRequest;
import ma.emsi.elevate.dto.response.InterviewResponse;
import ma.emsi.elevate.model.InterviewStatus;
import ma.emsi.elevate.service.InterviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST gérant les entretiens sur la plateforme Elevate.
 *
 * <p>Expose les endpoints suivants sous {@code /api/interviews} :</p>
 * <ul>
 *   <li>{@code POST /} : planifier un entretien (rôle RECRUITER)</li>
 *   <li>{@code GET /} : consulter ses entretiens (candidat ou recruteur)</li>
 *   <li>{@code PATCH /{interviewId}/status} : mettre à jour le statut d'un entretien</li>
 * </ul>
 *
 * <p>Tous les endpoints nécessitent une authentification (token JWT valide).</p>
 */
@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    /** Service de gestion des entretiens. */
    private final InterviewService interviewService;

    /**
     * Planifie un nouvel entretien pour une candidature donnée.
     * Réservé au recruteur propriétaire de l'offre associée à la candidature.
     * Une notification est envoyée automatiquement au candidat.
     *
     * @param request        les données de l'entretien (applicationId, date, lieu/lien)
     * @param authentication objet d'authentification Spring Security
     * @return HTTP 201 (Created) avec le DTO de l'entretien planifié
     */
    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<InterviewResponse> scheduleInterview(@Valid @RequestBody InterviewRequest request, Authentication authentication) {
        return new ResponseEntity<>(interviewService.scheduleInterview(request, authentication.getName()), HttpStatus.CREATED);
    }

    /**
     * Récupère la liste des entretiens de l'utilisateur connecté.
     * Selon le rôle : retourne les entretiens du candidat ou les entretiens du recruteur.
     *
     * @param authentication objet d'authentification Spring Security
     * @return HTTP 200 (OK) avec la liste des DTOs {@link InterviewResponse}
     */
    @GetMapping
    public ResponseEntity<List<InterviewResponse>> getMyInterviews(Authentication authentication) {
        return ResponseEntity.ok(interviewService.getMyInterviews(authentication.getName()));
    }

    /**
     * Met à jour le statut d'un entretien.
     * Une notification est envoyée automatiquement au candidat concerné.
     *
     * @param interviewId    identifiant de l'entretien
     * @param status         nouveau statut (SCHEDULED, COMPLETED ou CANCELED)
     * @param authentication objet d'authentification Spring Security
     * @return HTTP 200 (OK) avec le DTO de l'entretien mis à jour
     */
    @PatchMapping("/{interviewId}/status")
    public ResponseEntity<InterviewResponse> updateStatus(
            @PathVariable Long interviewId,
            @RequestParam InterviewStatus status,
            Authentication authentication) {
        return ResponseEntity.ok(interviewService.updateInterviewStatus(interviewId, status, authentication.getName()));
    }
}

