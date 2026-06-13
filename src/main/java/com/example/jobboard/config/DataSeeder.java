package com.example.jobboard.config;

import com.example.jobboard.model.*;
import com.example.jobboard.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Seeds demo data on first startup so the site is never empty.
 * Demo accounts:
 *   employer / employer123
 *   applicant / applicant123
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository users;
    private final CompanyRepository companies;
    private final JobPostRepository jobs;
    private final PasswordEncoder encoder;

    public DataSeeder(UserRepository users, CompanyRepository companies,
                      JobPostRepository jobs, PasswordEncoder encoder) {
        this.users = users; this.companies = companies; this.jobs = jobs; this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (jobs.count() > 0) return;

        User employer = users.findByUsername("employer").orElseGet(() -> {
            User u = new User();
            u.setUsername("employer");
            u.setEmail("employer@demo.io");
            u.setPasswordHash(encoder.encode("employer123"));
            u.setRole(Role.EMPLOYER);
            return users.save(u);
        });

        if (!users.existsByUsername("applicant")) {
            User a = new User();
            a.setUsername("applicant");
            a.setEmail("applicant@demo.io");
            a.setPasswordHash(encoder.encode("applicant123"));
            a.setRole(Role.APPLICANT);
            users.save(a);
        }

        Company nimbus = saveCompany(employer, "Nimbus Labs",
                "Cloud-native developer tools loved by 50k+ teams.", "https://nimbus.example.com");
        Company helios = saveCompany(employer, "Helios Energy",
                "Building the next generation of solar microgrids.", "https://helios.example.com");
        Company quanta = saveCompany(employer, "Quanta Health",
                "AI-assisted diagnostics for primary care clinics.", "https://quanta.example.com");
        Company orbit  = saveCompany(employer, "Orbit Studios",
                "Independent game studio behind 'Starbound Drift'.", "https://orbit.example.com");
        Company finch  = saveCompany(employer, "Finch Finance",
                "Modern banking for freelancers and small businesses.", "https://finch.example.com");

        // 15 jobs across the 5 companies
        addJob(nimbus, "Senior Backend Engineer (Java)", "Berlin, DE", "95000",
                "Design and scale our multi-tenant API platform. You'll own services from RFC to production, mentor engineers, and shape our event-driven architecture.\n\nStack: Java 21, Spring Boot, Kafka, PostgreSQL, Kubernetes.");
        addJob(nimbus, "DevOps Engineer", "Remote (EU)", "82000",
                "Run our Kubernetes platform on AWS and GCP. Improve CI/CD, observability, and incident response. Terraform, ArgoCD, Prometheus.");
        addJob(nimbus, "Frontend Engineer — React", "Berlin, DE", "78000",
                "Build the next generation of our developer dashboard. TypeScript, React 19, TanStack Query, Vite.");

        addJob(helios, "Embedded Firmware Engineer", "Munich, DE", "88000",
                "Write firmware for our solar inverter line. C/C++, FreeRTOS, low-level networking. Hardware-in-the-loop testing.");
        addJob(helios, "Field Operations Lead", "Lisbon, PT", "62000",
                "Lead deployment crews installing microgrids across southern Europe. Travel ~40%.");
        addJob(helios, "Data Engineer — Energy Analytics", "Remote", "90000",
                "Build the data pipelines powering our grid optimization models. dbt, Snowflake, Airflow, Python.");

        addJob(quanta, "Machine Learning Engineer", "Boston, MA", "135000",
                "Train and deploy clinical NLP models on de-identified EHR data. PyTorch, Hugging Face, MLflow.");
        addJob(quanta, "Clinical Product Manager", "Remote (US)", "120000",
                "Partner with primary care physicians to shape our diagnostics product. MD or RN background a plus.");
        addJob(quanta, "Senior QA Automation Engineer", "Boston, MA", "98000",
                "Own end-to-end testing for our regulated medical software. ISO 13485, Playwright, GitHub Actions.");

        addJob(orbit,  "Gameplay Programmer — Unreal", "Stockholm, SE", "72000",
                "Prototype and ship core combat mechanics for our upcoming title. Unreal Engine 5, C++, Blueprints.");
        addJob(orbit,  "Technical Artist", "Stockholm, SE", "68000",
                "Bridge art and engineering — shaders, tooling, performance. Substance, HLSL, Python.");
        addJob(orbit,  "Community Manager", "Remote", "48000",
                "Grow and nurture our Discord, social, and creator program. Excellent writing required.");

        addJob(finch,  "Compliance Officer", "London, UK", "75000",
                "Own our AML/KYC program as we expand into new markets. FCA experience required.");
        addJob(finch,  "iOS Engineer — Swift", "London, UK", "92000",
                "Ship beautiful, fast experiences on iOS. SwiftUI, Combine, modular architecture.");
        addJob(finch,  "Customer Success Specialist", "Remote (UK)", "42000",
                "Be the voice of Finch — onboard freelancers, resolve issues, surface product feedback.");
    }

    private Company saveCompany(User owner, String name, String desc, String site) {
        Company c = new Company();
        c.setName(name); c.setDescription(desc); c.setWebsite(site); c.setOwner(owner);
        return companies.save(c);
    }

    private void addJob(Company c, String title, String location, String salary, String desc) {
        JobPost j = new JobPost();
        j.setCompany(c); j.setTitle(title); j.setLocation(location);
        j.setSalary(new BigDecimal(salary)); j.setDescription(desc);
        j.setCreatedOn(LocalDateTime.now());
        jobs.save(j);
    }
}
