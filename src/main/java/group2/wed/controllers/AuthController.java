package group2.wed.controllers;

import group2.wed.authen.JwtProvider;
import group2.wed.constant.AppConstants;
import group2.wed.controllers.otherComponent.AppResponseException;
import group2.wed.controllers.otherComponent.Message;
import group2.wed.controllers.um.request.LoginRequest;
import group2.wed.controllers.um.request.ResetPasswordRequest;
import group2.wed.controllers.um.response.AppResponse;
import group2.wed.controllers.um.response.AppResponseFailure;
import group2.wed.controllers.um.response.AppResponseSuccess;
import group2.wed.entities.User;
import group2.wed.entities.dto.UserDTO;
import group2.wed.repository.UserRepository;
import group2.wed.services.CommonServices;
import group2.wed.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Random;

@RestController
@CrossOrigin
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CommonServices commonServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @PostMapping("/reset-password")
    public AppResponse resetPassword(@RequestBody ResetPasswordRequest request) {
        Message message = new Message();
        try {
            if (StringUtils.isEmpty(request.getUsername())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "username"));
            }
            if (StringUtils.isEmpty(request.getEmail())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "email"));
            }
            Optional<User> optionalUser = userService.findByUsername(request.getUsername());
            if (optionalUser.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "username"));
            }
            if (!request.getEmail().equals(optionalUser.get().getEmail())) {
                throw new AppResponseException(new Message(AppConstants.INVALID, "email"));
            }
            String newPass = randomString(6);
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPass));
            userRepository.save(user);
//            userService.resetPass(newPass, optionalUser.get().getUsername());
            String mailContent = "Your password from WED has been reset to" + newPass;
            String mailHeader = "Reset Password";
            commonServices.sendEmail(optionalUser.get().getEmail(), mailContent, mailHeader);
            return new AppResponseSuccess();
        } catch (Exception e) {
            return new AppResponseFailure(message);
        }
    }

    private String randomString(int length) {
        // create a string of all characters
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string

        for(int i = 0; i < length; i++) {
            // generate random index number
            int index = random.nextInt(alphabet.length());
            // get character specified by index
            // from the string
            char randomChar = alphabet.charAt(index);
            // append the character to string builder
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
