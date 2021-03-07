package group2.wed.controllers.other;

public class AppResponseException extends RuntimeException {
    public Message responseMessage;
    public AppResponseException(Message responseMessage) {
        super(responseMessage.getMessage()+ " " + responseMessage.getErrorCode());
        this.responseMessage = responseMessage;
    }

}
