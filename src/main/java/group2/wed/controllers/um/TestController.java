package group2.wed.controllers.um;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.um.request.GetUserTestRequest;
import group2.wed.controllers.um.response.GetUserTestResponse;
import group2.wed.entities.dto.UserDTO;
import group2.wed.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    private UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/get/detail")
    public GetUserTestResponse getDetail(@RequestBody GetUserTestRequest request) {
        try {
            System.out.println(request);
            GetUserTestResponse response = new GetUserTestResponse();
            UserDTO userDTO = userService.checkLogin(request.getUsername(), request.getPassword());
            response.setUserDTO(userDTO);
            response.setSuccess(AppConstants.RESULT_SUCCESS);
            return response;

        }  catch (Exception e) {
            GetUserTestResponse response = new GetUserTestResponse();
            response.setSuccess(AppConstants.RESULT_ERROR);
            System.out.println(e);
            return response;
        }
    }
    @GetMapping("/admin/get")
    public String getAdmin() {
        return "hi admin";
    }

    @GetMapping("/user/get")
    public String getUser() {
        return "hi user";
    }
}
