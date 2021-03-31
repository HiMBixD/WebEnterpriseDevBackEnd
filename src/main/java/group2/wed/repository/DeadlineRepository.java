package group2.wed.repository;

import group2.wed.entities.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DeadlineRepository extends JpaRepository<Deadline, Integer> {
    List<Deadline> findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(Date from, Date To);
}
