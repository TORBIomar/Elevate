package ma.emsi.elevate.service;

import ma.emsi.elevate.dto.response.ApplicationResponse;
import ma.emsi.elevate.model.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ApplicationService {

ApplicationResponse applyForJob(Long jobId, String username, MultipartFile cv, MultipartFile coverLetter);

Page<ApplicationResponse> getMyApplications(String username, Pageable pageable);

Page<ApplicationResponse> getApplicationsForJob(Long jobId, String username, Pageable pageable);

ApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatus status, String username);

void deleteApplication(Long id, String username);
}

