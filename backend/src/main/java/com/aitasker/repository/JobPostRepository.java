package com.aitasker.repository;

import com.aitasker.entity.JobPost;
import com.aitasker.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    List<JobPost> findByClientId(Long clientId);
    List<JobPost> findByStatus(JobStatus status);

    @Query("SELECT j FROM JobPost j WHERE " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:status IS NULL OR j.status = :status) AND " +
           "(:skills IS NULL OR LOWER(j.skillsRequired) LIKE LOWER(CONCAT('%', :skills, '%')))")
    List<JobPost> searchJobs(@Param("keyword") String keyword, @Param("status") JobStatus status, @Param("skills") String skills);
}
