package group2.wed.controllers.um;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.other.AppResponseException;
import group2.wed.controllers.other.Message;
import group2.wed.controllers.um.request.GetUserInfoRequest;
import group2.wed.controllers.um.request.UpdateUserInfoRequest;
import group2.wed.controllers.um.response.AppResponse;
import group2.wed.controllers.um.response.AppResponseFailure;
import group2.wed.controllers.um.response.AppResponseSuccess;
import group2.wed.entities.User;
import group2.wed.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserCommonController {
    @Autowired
    private UserService userService;

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

    @PostMapping("/get-user-info")
    public AppResponse getUserInfo(@RequestBody GetUserInfoRequest request) {
        try {
            return new AppResponseSuccess(userService.getUserInfo(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }
}
