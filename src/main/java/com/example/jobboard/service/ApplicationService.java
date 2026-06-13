package com.example.jobboard.service;
import com.example.jobboard.dto.ApplicationDto;
import com.example.jobboard.exception.*;
import com.example.jobboard.model.*;
import com.example.jobboard.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ApplicationService {
    private final ApplicationRepository repo;
    private final JobPostRepository jobs;
    private final UserService userService;
    public ApplicationService(ApplicationRepository repo, JobPostRepository jobs, UserService userService) {
        this.repo = repo; this.jobs = jobs; this.userService = userService;
    }

    public List<Application> listMine(UUID applicantId) { return repo.findAllByApplicantId(applicantId); }
    public List<Application> listForOwner(UUID ownerId) { return repo.findAllByJobPost_Company_OwnerId(ownerId); }
    public Application get(UUID id) { return repo.findById(id).orElseThrow(() -> new NotFoundException("Application not found")); }

    public Application apply(UUID jobId, ApplicationDto dto, UUID applicantId) {
        JobPost j = jobs.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found"));
        if (j.getCompany().getOwner().getId().equals(applicantId))
            throw new BusinessException("You cannot apply to your own job");
        if (repo.existsByApplicantIdAndJobPostId(applicantId, jobId))
            throw new BusinessException("You already applied to this job");
        Application a = new Application();
        a.setApplicant(userService.getById(applicantId));
        a.setJobPost(j);
        a.setCoverLetter(dto.getCoverLetter());
        a.setStatus(ApplicationStatus.PENDING);
        a.setSubmittedOn(LocalDateTime.now());
        return repo.save(a);
    }
    public Application updateCoverLetter(UUID id, ApplicationDto dto, UUID applicantId) {
        Application a = get(id);
        if (!a.getApplicant().getId().equals(applicantId)) throw new ForbiddenException("Not your application");
        if (a.getStatus() != ApplicationStatus.PENDING) throw new BusinessException("Cannot edit after decision");
        a.setCoverLetter(dto.getCoverLetter());
        return repo.save(a);
    }
    public Application decide(UUID id, ApplicationStatus status, UUID ownerId) {
        Application a = get(id);
        if (!a.getJobPost().getCompany().getOwner().getId().equals(ownerId))
            throw new ForbiddenException("Not your job");
        if (status == ApplicationStatus.PENDING) throw new BusinessException("Invalid status");
        a.setStatus(status);
        return repo.save(a);
    }
    public void withdraw(UUID id, UUID applicantId) {
        Application a = get(id);
        if (!a.getApplicant().getId().equals(applicantId)) throw new ForbiddenException("Not your application");
        repo.delete(a);
    }
}
