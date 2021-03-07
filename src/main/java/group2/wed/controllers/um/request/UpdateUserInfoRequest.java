package group2.wed.controllers.um.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Long roleId;
}
