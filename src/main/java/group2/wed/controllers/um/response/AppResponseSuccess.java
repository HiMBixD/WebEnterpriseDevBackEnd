package group2.wed.controllers.um.response;

import group2.wed.constant.AppConstants;

public class AppResponseSuccess extends AppResponse{
    public AppResponseSuccess(){
        this.setSuccess(AppConstants.RESULT_SUCCESS);
    }
    public AppResponseSuccess(Object data){
        this.setSuccess(AppConstants.RESULT_SUCCESS);
        this.setData(data);
    }

}
