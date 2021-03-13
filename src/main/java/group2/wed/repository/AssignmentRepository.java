package group2.wed.repository;

import group2.wed.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    @Query("from Assignment a where a.assignmentId = :assignmentId")
    Optional<Assignment> findAssignmentById(@Param("assignmentId") Long assignmentId);
}
