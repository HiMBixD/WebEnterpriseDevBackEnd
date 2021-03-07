package group2.wed.controllers.um;

import group2.wed.controllers.other.AppResponseException;
import group2.wed.controllers.um.request.CreateUserRequest;
import group2.wed.controllers.um.request.UpdateUserInfoRequest;
import group2.wed.controllers.um.response.AppResponse;
import group2.wed.controllers.um.response.AppResponseFailure;
import group2.wed.controllers.um.response.AppResponseSuccess;
import group2.wed.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public AppResponse registerUser(@RequestBody CreateUserRequest createUserRequest) {
        try {
            return new AppResponseSuccess(userService.createUser(createUserRequest));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }

    @PostMapping("/update-user-info")
    public AppResponse updateUserInfo(@RequestBody UpdateUserInfoRequest request) {
        try {
            return new AppResponseSuccess(userService.updateUserInfo(request, true));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }
}
