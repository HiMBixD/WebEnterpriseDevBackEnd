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
@Table(name = "comment")
public class Comment {
    @Id
    @Column(name = "comment_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer commentId; // year as id

    @Column(name = "created_date")
    private Date createDate;

    @Column(name = "content")
    private String content;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "submission_id")
    private Integer submissionId;

}
