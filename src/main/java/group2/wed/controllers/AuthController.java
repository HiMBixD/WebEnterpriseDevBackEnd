package group2.wed.controllers;

import group2.wed.authen.JwtProvider;
import group2.wed.constant.AppConstants;
import group2.wed.controllers.other.Message;
import group2.wed.controllers.um.request.LoginRequest;
import group2.wed.controllers.um.response.AppResponse;
import group2.wed.controllers.um.response.AppResponseFailure;
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
        Message message = new Message();
        try {
            UserDTO userDTO = userService.findByUsernameAndPassword(request.getUsername(), request.getPassword());
            if (null == userDTO) {
                message.setMessage(AppConstants.NOT_FOUND);
                message.setErrorCode("wrongIDorPassword");
            }
            assert userDTO != null;
            String token = jwtProvider.generateToken(userDTO.getUserName());
            return new AppResponseSuccess(token);
        } catch (Exception e) {
            return new AppResponseFailure(message);
        }
    }
}
