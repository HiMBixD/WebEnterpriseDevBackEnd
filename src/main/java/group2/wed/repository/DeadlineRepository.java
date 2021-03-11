package group2.wed.repository;

import group2.wed.entities.Assignment;
import group2.wed.entities.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeadlineRepository extends JpaRepository<Deadline, Integer> {
    @Query("from Deadline d where d.year = :year")
    Optional<Deadline> findDeadlineById(@Param("year") Long year);
}
