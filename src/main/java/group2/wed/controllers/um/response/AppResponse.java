package group2.wed.controllers.um.response;

import group2.wed.controllers.otherComponent.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppResponse {
    private Boolean success;
    private Message responseMessage;
    private Object data;
}
