package group2.wed.controllers.um.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
}
