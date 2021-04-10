package group2.wed.entities.dto;

import group2.wed.entities.Submission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionDTO extends Submission {
    private Integer totalComment;

    public SubmissionDTO(Submission submission, Integer totalComment) {
        this.setStatus(submission.getStatus());
        this.setAssignmentId(submission.getAssignmentId());
        this.setSubmissionDate(submission.getSubmissionDate());
        this.setSubmissionId(submission.getSubmissionId());
        this.setUsername(submission.getUsername());
        this.totalComment = totalComment;
    }
}
