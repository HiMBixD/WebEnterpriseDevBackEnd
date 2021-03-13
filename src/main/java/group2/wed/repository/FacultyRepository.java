package group2.wed.repository;

import group2.wed.entities.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    @Query("from Faculty f where f.facultyId = :facultyId")
    Optional<Faculty> findFaById(@Param("facultyId") Long facultyId);
}
