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
 * Controleur REST des entretiens.
 */
@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

private final InterviewService interviewService;

    /**
     * Planifie un entretien (recruteur).
     */
    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<InterviewResponse> scheduleInterview(@Valid @RequestBody InterviewRequest request, Authentication authentication) {
        return new ResponseEntity<>(interviewService.scheduleInterview(request, authentication.getName()), HttpStatus.CREATED);
    }

    /**
     * Liste les entretiens de l'utilisateur connecte.
     */
    @GetMapping
    public ResponseEntity<List<InterviewResponse>> getMyInterviews(Authentication authentication) {
        return ResponseEntity.ok(interviewService.getMyInterviews(authentication.getName()));
    }

    /**
     * Met a jour le statut d'un entretien.
     */
    @PatchMapping("/{interviewId}/status")
    public ResponseEntity<InterviewResponse> updateStatus(
            @PathVariable Long interviewId,
            @RequestParam InterviewStatus status,
            Authentication authentication) {
        return ResponseEntity.ok(interviewService.updateInterviewStatus(interviewId, status, authentication.getName()));
    }
}

