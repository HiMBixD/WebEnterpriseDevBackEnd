package group2.wed.controllers.um.response;

import group2.wed.constant.AppConstants;

public class AppResponseFailure extends AppResponse{
    public AppResponseFailure(){
        this.setSuccess(AppConstants.RESULT_ERROR);
    }
}
