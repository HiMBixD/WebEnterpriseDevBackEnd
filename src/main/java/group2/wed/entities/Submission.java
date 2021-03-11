package group2.wed.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "submission")
public class Submission {
    @Id
    @Column(name = "submission_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer submissionId; // year as id

    @Column(name = "submission_date")
    private Date submissionDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(name = "username")
    private String username;
}
