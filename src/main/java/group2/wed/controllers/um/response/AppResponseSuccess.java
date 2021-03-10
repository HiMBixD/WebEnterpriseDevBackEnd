package group2.wed.controllers.um.response;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.otherComponent.Message;

public class AppResponseSuccess extends AppResponse{
    public AppResponseSuccess(){
        this.setSuccess(AppConstants.RESULT_SUCCESS);
    }
    public AppResponseSuccess(Object data){
        this.setSuccess(AppConstants.RESULT_SUCCESS);
        this.setData(data);
    }
    public AppResponseSuccess(Message message){
        this.setSuccess(AppConstants.RESULT_SUCCESS);
        this.setResponseMessage(message);
    }

}
