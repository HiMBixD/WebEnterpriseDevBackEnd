package group2.wed.controllers.um.response;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.otherComponent.Message;

public class AppResponseFailure extends AppResponse{
    public AppResponseFailure(){
        this.setSuccess(AppConstants.RESULT_ERROR);
    }
    public AppResponseFailure(Message mess){
        this.setSuccess(AppConstants.RESULT_ERROR);
        this.setResponseMessage(mess);
    }
    public AppResponseFailure(String mess){
        this.setSuccess(AppConstants.RESULT_ERROR);
        this.setResponseMessage(new Message(mess));
    }
}
