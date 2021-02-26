package group2.wed.entities;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Column(name = "PASSWORD", length = 100)
    private String password;

    @Column(name = "FIRST_NAME", length = 100)
    private String firstName;

    @Column(name = "LAST_NAME", length = 100)
    private String lastName;

    @Column(name = "PHONE", length = 15)
    private String phone;


    @Column(name = "EMAIL", length = 200)
    private String email;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private RoleEntity roleEntity;
}
