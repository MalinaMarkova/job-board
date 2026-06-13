package com.example.jobboard.repository;
import com.example.jobboard.model.Company;
import com.example.jobboard.model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface JobPostRepository extends JpaRepository<JobPost, UUID> {
    List<JobPost> findAllByOrderByCreatedOnDesc();
    List<JobPost> findAllByCompany_OwnerId(UUID ownerId);
}
