package group2.wed.controllers.um.response;

import group2.wed.entities.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserTestResponse {
    private UserDTO userDTO;
}
