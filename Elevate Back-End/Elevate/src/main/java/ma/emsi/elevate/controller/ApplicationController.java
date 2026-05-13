package ma.emsi.elevate.controller;

import lombok.RequiredArgsConstructor;
import ma.emsi.elevate.dto.response.ApplicationResponse;
import ma.emsi.elevate.model.ApplicationStatus;
import ma.emsi.elevate.service.ApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Contrôleur REST gérant les candidatures aux offres d'emploi sur la plateforme Elevate.
 *
 * <p>Expose les endpoints suivants sous {@code /api/applications} :</p>
 * <ul>
 *   <li>{@code POST /{jobId}} : soumettre une candidature (rôle CANDIDATE)</li>
 *   <li>{@code GET /my-applications} : consulter ses candidatures (rôle CANDIDATE)</li>
 *   <li>{@code GET /job/{jobId}} : voir les candidatures d'une offre (rôle RECRUITER)</li>
 *   <li>{@code PATCH /{applicationId}/status} : mettre à jour le statut (rôle RECRUITER)</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    /** Service de gestion des candidatures. */
    private final ApplicationService applicationService;

    /**
     * Soumet une candidature pour une offre d'emploi.
     * Accessible uniquement aux utilisateurs ayant le rôle {@code CANDIDATE}.
     *
     * @param jobId          identifiant de l'offre d'emploi
     * @param cv             fichier CV (obligatoire, multipart)
     * @param coverLetter    lettre de motivation (optionnel, multipart)
     * @param authentication objet d'authentification Spring Security
     * @return HTTP 201 (Created) avec le DTO de la candidature créée
     */
    @PostMapping("/{jobId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponse> apply(
            @PathVariable Long jobId,
            @RequestParam("cv") MultipartFile cv,
            @RequestParam(value = "coverLetter", required = false) MultipartFile coverLetter,
            Authentication authentication) {
        return new ResponseEntity<>(applicationService.applyForJob(jobId, authentication.getName(), cv, coverLetter), HttpStatus.CREATED);
    }

    /**
     * Récupère la liste paginée des candidatures du candidat connecté.
     * Accessible uniquement aux utilisateurs ayant le rôle {@code CANDIDATE}.
     *
     * @param authentication objet d'authentification Spring Security
     * @param pageable       paramètres de pagination et de tri
     * @return HTTP 200 (OK) avec la page de DTOs de candidatures
     */
    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Page<ApplicationResponse>> getMyApplications(Authentication authentication, Pageable pageable) {
        return ResponseEntity.ok(applicationService.getMyApplications(authentication.getName(), pageable));
    }

    /**
     * Récupère la liste paginée des candidatures reçues pour une offre d'emploi.
     * Accessible uniquement au recruteur propriétaire de l'offre.
     *
     * @param jobId          identifiant de l'offre d'emploi
     * @param authentication objet d'authentification Spring Security
     * @param pageable       paramètres de pagination et de tri
     * @return HTTP 200 (OK) avec la page de DTOs de candidatures
     */
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Page<ApplicationResponse>> getApplicationsForJob(
            @PathVariable Long jobId,
            Authentication authentication,
            Pageable pageable) {
        return ResponseEntity.ok(applicationService.getApplicationsForJob(jobId, authentication.getName(), pageable));
    }

    /**
     * Met à jour le statut d'une candidature.
     * Accessible uniquement au recruteur propriétaire de l'offre associée.
     * Une notification est envoyée automatiquement au candidat.
     *
     * @param applicationId  identifiant de la candidature
     * @param status         nouveau statut à appliquer
     * @param authentication objet d'authentification Spring Security
     * @return HTTP 200 (OK) avec le DTO de la candidature mise à jour
     */
    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus status,
            Authentication authentication) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(applicationId, status, authentication.getName()));
    }
}

