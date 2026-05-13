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
 * Controleur REST pour la gestion des candidatures.
 */
@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

private final ApplicationService applicationService;

    /**
     * Candidature a une offre avec fichiers (CV/lettre).
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
     * Liste les candidatures du candidat connecte (pagine).
     */
    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Page<ApplicationResponse>> getMyApplications(Authentication authentication, Pageable pageable) {
        return ResponseEntity.ok(applicationService.getMyApplications(authentication.getName(), pageable));
    }

    /**
     * Liste les candidatures recues pour une offre (recruteur).
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
     * Met a jour le statut d'une candidature.
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

