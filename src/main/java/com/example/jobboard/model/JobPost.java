package com.example.jobboard.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@Entity
@Table(name = "job_posts")
public class JobPost {
    @Id @GeneratedValue private UUID id;
    @Column(nullable = false, length = 120)
    private String title;
    @Column(nullable = false, length = 2000)
    private String description;
    @Column(nullable = false, length = 80)
    private String location;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;
    @Column(nullable = false)
    private LocalDateTime createdOn;
    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


}
