package group2.wed.controllers;

import group2.wed.authen.JwtProvider;
import group2.wed.controllers.um.request.LoginRequest;
import group2.wed.controllers.um.response.AppResponse;
import group2.wed.controllers.um.response.AppResponseSuccess;
import group2.wed.entities.dto.UserDTO;
import group2.wed.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;


    @PostMapping("/auth")
    public AppResponse auth(@RequestBody LoginRequest request) {
        UserDTO userDTO = userService.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        String token = jwtProvider.generateToken(userDTO.getUserName());
        AppResponse response = new AppResponseSuccess(token);
        return response;
    }
}
