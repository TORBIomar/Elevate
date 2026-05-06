package ma.emsi.elevate.service;

import ma.emsi.elevate.dto.request.JobOfferRequest;
import ma.emsi.elevate.dto.response.JobOfferResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobOfferService {
    JobOfferResponse createJobOffer(JobOfferRequest request, String recruiterEmail);
    JobOfferResponse updateJobOffer(Long id, JobOfferRequest request, String recruiterEmail);
    void deleteJobOffer(Long id, String recruiterEmail);
    JobOfferResponse getJobOfferById(Long id);
    Page<JobOfferResponse> searchJobOffers(String keyword, String category, Pageable pageable);
    Page<JobOfferResponse> getJobOffersByRecruiter(Long recruiterId, Pageable pageable);
}

