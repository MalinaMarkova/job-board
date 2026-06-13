package com.example.jobboard.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_posts")
public class JobPost {
    @Id @GeneratedValue private UUID id;
    @Column(nullable = false, length = 120) private String title;
    @Column(nullable = false, length = 2000) private String description;
    @Column(nullable = false, length = 80) private String location;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal salary;
    @Column(nullable = false) private LocalDateTime createdOn;
    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false) private Company company;

    public UUID getId() { return id; }
    public String getTitle() { return title; } public void setTitle(String v) { this.title = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public String getLocation() { return location; } public void setLocation(String v) { this.location = v; }
    public BigDecimal getSalary() { return salary; } public void setSalary(BigDecimal v) { this.salary = v; }
    public LocalDateTime getCreatedOn() { return createdOn; } public void setCreatedOn(LocalDateTime v) { this.createdOn = v; }
    public Company getCompany() { return company; } public void setCompany(Company v) { this.company = v; }
}
