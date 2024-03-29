package group2.wed.entities.dto;

import group2.wed.entities.RoleEntity;
import group2.wed.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userName;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private Long facultyId;

    private RoleDTO role;

    public UserDTO(User user) {
        this.userName = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.facultyId = user.getFacultyId();
        this.role = new RoleDTO(user.getRoleEntity());
    }
}
