package group2.wed.repository;

import group2.wed.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository  extends JpaRepository<Assignment, Integer> {
}
