package group2.wed.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ASSIGNMENT")
public class Assignment {
    @Id
    @Column(name = "ASSIGNMENT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long assignmentId;

    @Column(name = "ASSIGNMENT_NAME", length = 30, nullable = false)
    private String assignmentName;

    @Column(name = "CREATE_BY", length = 6, nullable = false)
    private String create_by;

    @Column(name = "deadline_id", nullable = false)
    private Integer deadlineId;

    @Column(name = "FACULTY_ID", nullable = false)
    private Long facultyId;
}
