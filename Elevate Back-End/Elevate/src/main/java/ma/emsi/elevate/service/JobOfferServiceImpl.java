package ma.emsi.elevate.service;

import ma.emsi.elevate.dto.request.JobOfferRequest;
import ma.emsi.elevate.dto.response.JobOfferResponse;
import ma.emsi.elevate.mapper.JobOfferMapper;
import ma.emsi.elevate.model.JobOffer;
import ma.emsi.elevate.model.User;
import ma.emsi.elevate.repository.JobOfferRepository;
import ma.emsi.elevate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobOfferServiceImpl implements JobOfferService {

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobOfferMapper jobOfferMapper;

@Override
    public JobOfferResponse createJobOffer(JobOfferRequest request, String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        JobOffer jobOffer = jobOfferMapper.toEntity(request);
        jobOffer.setRecruiter(recruiter);

        JobOffer savedOffer = jobOfferRepository.save(jobOffer);
        return jobOfferMapper.toResponse(savedOffer);
    }

@Override
    public JobOfferResponse updateJobOffer(Long id, JobOfferRequest request, String recruiterEmail) {
        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job offer not found"));

        if (!jobOffer.getRecruiter().getEmail().equals(recruiterEmail)) {
            throw new RuntimeException("You are not authorized to update this offer");
        }

        jobOffer.setTitle(request.getTitle());
        jobOffer.setDescription(request.getDescription());
        jobOffer.setLocation(request.getLocation());
        jobOffer.setJobType(request.getJobType());
        jobOffer.setCategory(request.getCategory());
        jobOffer.setSalary(request.getSalary());

        return jobOfferMapper.toResponse(jobOfferRepository.save(jobOffer));
    }

@Override
    public void deleteJobOffer(Long id, String recruiterEmail) {
        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job offer not found"));

        if (!jobOffer.getRecruiter().getEmail().equals(recruiterEmail)) {
            throw new RuntimeException("You are not authorized to delete this offer");
        }

        jobOfferRepository.delete(jobOffer);
    }

@Override
    @Transactional(readOnly = true)
    public JobOfferResponse getJobOfferById(Long id) {
        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job offer not found"));
        return jobOfferMapper.toResponse(jobOffer);
    }

@Override
    @Transactional(readOnly = true)
    public Page<JobOfferResponse> searchJobOffers(String keyword, String category, Pageable pageable) {
        return jobOfferRepository.searchOffers(keyword, category, pageable)
                .map(jobOfferMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobOfferResponse> getJobOffersByRecruiter(Long recruiterId, Pageable pageable) {
        return jobOfferRepository.findByRecruiterId(recruiterId, pageable)
                .map(jobOfferMapper::toResponse);
    }
}
