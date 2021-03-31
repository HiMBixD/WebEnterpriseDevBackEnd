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
@Table(name = "DEADLINE")
public class Deadline {
    @Id
    @Column(name = "deadline_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer deadlineId;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "start_date", nullable = false)
    private Date startDate;
}
