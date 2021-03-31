package group2.wed.controllers.um;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.otherComponent.AppResponseException;
import group2.wed.controllers.otherComponent.Message;
import group2.wed.controllers.um.request.*;
import group2.wed.controllers.um.response.AppResponse;
import group2.wed.controllers.um.response.AppResponseFailure;
import group2.wed.controllers.um.response.AppResponseSuccess;
import group2.wed.services.CommonServices;
import group2.wed.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserCommonController {
    @Autowired
    private UserService userService;
    @Autowired
    private CommonServices commonServices;

    @PostMapping("/get-my-info")
    public AppResponse getMyInfo() {
        try {
            UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            GetUserInfoRequest request = new GetUserInfoRequest();
            request.setUsername(user.getUsername());
            return new AppResponseSuccess(userService.getUserInfo(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }

    @PostMapping("/update-my-info")
    public AppResponse updateMyInfo(@RequestBody UpdateUserInfoRequest request) {
        try {
            UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!user.getUsername().equals(request.getUsername())) {
                throw new AppResponseException(new Message(AppConstants.INVALID, "username"));
            }
            return new AppResponseSuccess(userService.updateUserInfo(request, false));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }

    @PostMapping("/change-password")
    public AppResponse changePass(@RequestBody ChangePassRequest request) {
        try {
            UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!user.getUsername().equals(request.getUsername())) {
                throw new AppResponseException(new Message(AppConstants.INVALID, "username"));
            }
            userService.changePass(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }

    @PostMapping("/get-user-info")
    public AppResponse getUserInfo(@RequestBody GetUserInfoRequest request) {
        try {
            return new AppResponseSuccess(userService.getUserInfo(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }

    @PostMapping("/get-deadline")
    public AppResponse getDeadline(@RequestBody GetDeadLineRequest request) {
        try {
            return new AppResponseSuccess(commonServices.getDeadLine(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }

    @PostMapping("/get-list-deadline")
    public AppResponse getListDeadline(@RequestBody GetListDeadLineRequest request) {
        try {
            return new AppResponseSuccess(commonServices.getListDeadLine(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }
}
