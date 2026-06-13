package com.example.jobboard.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "applications", uniqueConstraints = @UniqueConstraint(columnNames = {"applicant_id","job_post_id"}))
public class Application {
    @Id @GeneratedValue private UUID id;
    @Column(nullable = false, length = 1500) private String coverLetter;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private ApplicationStatus status;
    @Column(nullable = false) private LocalDateTime submittedOn;
    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "applicant_id", nullable = false) private User applicant;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "job_post_id", nullable = false) private JobPost jobPost;

    public UUID getId() { return id; }
    public String getCoverLetter() { return coverLetter; } public void setCoverLetter(String v) { this.coverLetter = v; }
    public ApplicationStatus getStatus() { return status; } public void setStatus(ApplicationStatus v) { this.status = v; }
    public LocalDateTime getSubmittedOn() { return submittedOn; } public void setSubmittedOn(LocalDateTime v) { this.submittedOn = v; }
    public User getApplicant() { return applicant; } public void setApplicant(User v) { this.applicant = v; }
    public JobPost getJobPost() { return jobPost; } public void setJobPost(JobPost v) { this.jobPost = v; }
}
