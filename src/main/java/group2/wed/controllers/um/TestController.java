package group2.wed.controllers.um;

import group2.wed.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    private UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
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
