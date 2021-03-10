package group2.wed.controllers.otherComponent;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message {
    private String message;
    private String errorCode;

    public Message(String errorCode, String message) {
        this.setErrorCode(errorCode);
        this.setMessage(message);
    }

    public Message(String message) {
        this.setMessage(message);
    }
}
