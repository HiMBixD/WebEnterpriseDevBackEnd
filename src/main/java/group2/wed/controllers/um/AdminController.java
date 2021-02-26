package group2.wed.controllers.um;

import group2.wed.authen.JwtProvider;
import group2.wed.controllers.um.request.CreateUserRequest;
import group2.wed.controllers.um.response.AppResponse;
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
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/create-user")
    public AppResponse registerUser(@RequestBody CreateUserRequest createUserRequest) throws Exception {
        try {
            AppResponse appResponse = new AppResponse();
            appResponse.setSuccess(userService.createUser(createUserRequest));
            return appResponse;

        }  catch (Exception e) {
            throw e;
        }
    }
}
