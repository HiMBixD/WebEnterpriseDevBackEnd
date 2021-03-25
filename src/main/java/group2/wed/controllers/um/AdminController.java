package group2.wed.controllers.um;

import group2.wed.controllers.otherComponent.AppResponseException;
import group2.wed.controllers.um.request.CreateUserRequest;
import group2.wed.controllers.um.request.SearchUserRequest;
import group2.wed.controllers.um.request.SetClosureDateRequest;
import group2.wed.controllers.um.request.UpdateUserInfoRequest;
import group2.wed.controllers.um.response.AppResponse;
import group2.wed.controllers.um.response.AppResponseFailure;
import group2.wed.controllers.um.response.AppResponseSuccess;
import group2.wed.entities.dto.UserDTO;
import group2.wed.services.CommonServices;
import group2.wed.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private CommonServices commonServices;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/create-user")
    public AppResponse registerUser(@RequestBody CreateUserRequest createUserRequest) {
        try {
            UserDTO userDTO = userService.createUser(createUserRequest);
            String mailContent = "<p><strong><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Dear Mr/Mrs "+userDTO.getFirstName()+",</span></span></span></strong></p>" +
                    "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">You are now able to login to our website, your account information is as follow: </span></span></span></p>" +
                    "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Username: <strong>"+ userDTO.getUserName() + "</strong></span></span></span></p>" +
                    "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Password: <strong>"+ createUserRequest.getPassword() + "</strong></span></span></span></p>" +
                    "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Please login to WED web to confirm your account information with the link below</span></span></span></p>" +
                    "<p>https://wed-front-end.herokuapp.com/</p>" +
                    "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Thank you for using our service,</span></span></span></p>" +
                    "<p><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"font-size:14px\"><span style=\"color:#000000\">Best regards.</span></span></span></p>";
            String mailHeader = "New Account created on WED website";
            commonServices.sendEmail(userDTO.getEmail(),mailContent,mailHeader);
            return new AppResponseSuccess(userDTO);
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        } catch (Exception e) {
            return new AppResponseFailure(e.getMessage());
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

    @PostMapping("/search-users")
    public AppResponse getUsersInfo(@RequestBody SearchUserRequest request) {
        try {
            return new AppResponseSuccess(userService.searchUsersInfo(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }

    @PostMapping("/set-closure")
    public AppResponse setClosureDate(@RequestBody SetClosureDateRequest request) {
        try {
            return new AppResponseSuccess(commonServices.setClosureDate(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }
}
