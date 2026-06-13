package com.example.jobboard.repository;
import com.example.jobboard.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    List<Application> findAllByApplicantId(UUID applicantId);
    List<Application> findAllByJobPost_Company_OwnerId(UUID ownerId);
    boolean existsByApplicantIdAndJobPostId(UUID applicantId, UUID jobPostId);
}
