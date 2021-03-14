package group2.wed.repository;

import group2.wed.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    @Query("from Assignment a where a.assignmentId = :assignmentId")
    Optional<Assignment> findAssignmentById(@Param("assignmentId") Long assignmentId);

    @Query("from Assignment a where a.assignmentName = :assignmentName")
    Optional<Assignment> findAssignmentByName(@Param("assignmentName")String assignmentName);

    @Query("FROM Assignment a WHERE (:facultyId is null or a.facultyId = :facultyId) and (:deadlineId is null or a.deadlineId = :deadlineId)")
    List<Assignment> searchAssignmentByFaOrYear(@Param("facultyId")Long facultyId, @Param("deadlineId")Integer deadlineId);
}
