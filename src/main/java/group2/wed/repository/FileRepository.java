package group2.wed.repository;

import group2.wed.entities.File;
import group2.wed.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Integer> {
    @Query("FROM File f WHERE (:submissionId is null or f.submissionId = :submissionId)")
    List<File> getAllBySubmissionId(@Param("submissionId")Integer submissionId);
}
