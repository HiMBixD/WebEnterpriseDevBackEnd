package group2.wed.controllers.um.request;

import group2.wed.entities.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserTestRequest {
    private String username;
    private String password;
}
