package com.example.jobboard.service;
import com.example.jobboard.dto.CompanyDto;
import com.example.jobboard.exception.*;
import com.example.jobboard.model.*;
import com.example.jobboard.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CompanyService {
    private final CompanyRepository repo;
    public CompanyService(CompanyRepository repo) { this.repo = repo; }

    public List<Company> listAll() { return repo.findAll(); }
    public List<Company> listMine(User owner) { return repo.findAllByOwner(owner); }
    public Company get(UUID id) { return repo.findById(id).orElseThrow(() -> new NotFoundException("Company not found")); }

    public Company create(CompanyDto dto, User owner) {
        Company c = new Company();
        apply(dto, c);
        c.setOwner(owner);
        return repo.save(c);
    }
    public Company update(UUID id, CompanyDto dto, UUID userId) {
        Company c = get(id);
        if (!c.getOwner().getId().equals(userId)) throw new ForbiddenException("Not your company");
        apply(dto, c);
        return repo.save(c);
    }
    public void delete(UUID id, UUID userId) {
        Company c = get(id);
        if (!c.getOwner().getId().equals(userId)) throw new ForbiddenException("Not your company");
        repo.delete(c);
    }
    private void apply(CompanyDto dto, Company c) {
        c.setName(dto.getName()); c.setDescription(dto.getDescription()); c.setWebsite(dto.getWebsite());
    }
}
