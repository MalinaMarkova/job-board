package com.example.jobboard.service;
import com.example.jobboard.dto.JobPostDto;
import com.example.jobboard.exception.*;
import com.example.jobboard.model.*;
import com.example.jobboard.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class JobPostService {
    private final JobPostRepository repo;
    private final CompanyRepository companies;
    public JobPostService(JobPostRepository repo, CompanyRepository companies) { this.repo = repo; this.companies = companies; }

    public List<JobPost> listAll() { return repo.findAllByOrderByCreatedOnDesc(); }
    public List<JobPost> listByOwner(UUID ownerId) { return repo.findAllByCompany_OwnerId(ownerId); }
    public JobPost get(UUID id) { return repo.findById(id).orElseThrow(() -> new NotFoundException("Job not found")); }

    public JobPost create(JobPostDto dto, UUID userId) {
        Company c = companies.findById(dto.getCompanyId()).orElseThrow(() -> new NotFoundException("Company not found"));
        if (!c.getOwner().getId().equals(userId)) throw new ForbiddenException("Not your company");
        JobPost j = new JobPost();
        apply(dto, j); j.setCompany(c); j.setCreatedOn(LocalDateTime.now());
        return repo.save(j);
    }
    public JobPost update(UUID id, JobPostDto dto, UUID userId) {
        JobPost j = get(id);
        if (!j.getCompany().getOwner().getId().equals(userId)) throw new ForbiddenException("Not your post");
        apply(dto, j);
        return repo.save(j);
    }
    public void delete(UUID id, UUID userId) {
        JobPost j = get(id);
        if (!j.getCompany().getOwner().getId().equals(userId)) throw new ForbiddenException("Not your post");
        repo.delete(j);
    }
    private void apply(JobPostDto dto, JobPost j) {
        j.setTitle(dto.getTitle()); j.setDescription(dto.getDescription());
        j.setLocation(dto.getLocation()); j.setSalary(dto.getSalary());
    }
}
