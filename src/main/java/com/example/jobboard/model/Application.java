package com.example.jobboard.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
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

}
