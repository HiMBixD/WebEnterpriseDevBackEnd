package group2.wed.repository;

import group2.wed.entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    @Query("from Submission s where s.submissionId = :submissionId")
    Optional<Submission> findSubmissionById(@Param("submissionId")Long submissionId);

    @Query("FROM Submission s WHERE s.username LIKE CONCAT('%',:username,'%') and (:status is null or s.status = :status)")
    List<Submission> searchByUsernameOrStatus(@Param("username")String username, @Param("status")Integer status);
}
