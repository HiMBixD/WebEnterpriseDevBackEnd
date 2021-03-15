package group2.wed.repository;

import group2.wed.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment, Integer> {
    @Query("FROM Comment c WHERE c.submissionId = :submissionId")
    List<Comment> getAllBySubmissionId(@Param("submissionId")Integer submissionId);
}
