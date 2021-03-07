package group2.wed.controllers.um.response;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.other.Message;

public class AppResponseFailure extends AppResponse{
    public AppResponseFailure(){
        this.setSuccess(AppConstants.RESULT_ERROR);
    }
    public AppResponseFailure(Message mess){
        this.setSuccess(AppConstants.RESULT_ERROR);
        this.setResponseMessage(mess);
    }
}
