package ma.emsi.elevate.controller;

import jakarta.validation.Valid;
import ma.emsi.elevate.dto.request.JobOfferRequest;
import ma.emsi.elevate.dto.response.JobOfferResponse;
import ma.emsi.elevate.service.JobOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobOfferController {

    @Autowired
    private JobOfferService jobOfferService;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobOfferResponse> createJobOffer(@Valid @RequestBody JobOfferRequest request, Authentication authentication) {
        JobOfferResponse response = jobOfferService.createJobOffer(request, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobOfferResponse> updateJobOffer(
            @PathVariable Long id,
            @Valid @RequestBody JobOfferRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(jobOfferService.updateJobOffer(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Void> deleteJobOffer(@PathVariable Long id, Authentication authentication) {
        jobOfferService.deleteJobOffer(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobOfferResponse> getJobOffer(@PathVariable Long id) {
        return ResponseEntity.ok(jobOfferService.getJobOfferById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<JobOfferResponse>> searchJobOffers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            Pageable pageable) {
        return ResponseEntity.ok(jobOfferService.searchJobOffers(keyword, category, pageable));
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<Page<JobOfferResponse>> getJobOffersByRecruiter(
            @PathVariable Long recruiterId,
            Pageable pageable) {
        return ResponseEntity.ok(jobOfferService.getJobOffersByRecruiter(recruiterId, pageable));
    }
}

