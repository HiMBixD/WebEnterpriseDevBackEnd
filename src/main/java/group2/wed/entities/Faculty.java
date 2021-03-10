package group2.wed.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FACULTY")
public class Faculty {

    @Id
    @Column(name = "FACULTY_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long facultyId;

    @Column(name = "FACULTY_NAME", length = 30, nullable = false)
    private String facultyName;
}
