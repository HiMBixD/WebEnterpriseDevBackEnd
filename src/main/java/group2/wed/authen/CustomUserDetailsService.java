package group2.wed.authen;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.otherComponent.AppResponseException;
import group2.wed.controllers.otherComponent.Message;
import group2.wed.entities.User;
import group2.wed.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        Optional<User> optional = userService.findByUsername(username);
        if (optional.isEmpty()) {
            throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
        }
        return CustomUserDetails.fromUserEntityToCustomUserDetails(optional.get());
    }
}
