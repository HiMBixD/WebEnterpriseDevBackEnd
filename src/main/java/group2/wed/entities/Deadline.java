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
    private Long year; // year as id

    @Column(name = "closure_date")
    private Date closureDate;
}
