package group2.wed.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ROLES")
@Data
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(name = "ROLE_NAME", length = 25, nullable = false)
    private String roleName;

    @Column(name = "description")
    private String description;
}