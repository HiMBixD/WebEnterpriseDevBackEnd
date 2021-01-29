package group2.wed.services;

import group2.wed.entities.User;
import group2.wed.entities.dto.UserDTO;
import group2.wed.repository.UserRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.ServiceNotFoundException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDTO checkLogin(String userName, String password) throws Exception {
        UserDTO userDTO = new UserDTO();
        Optional<User> optional = userRepository.findByUserNameAndPassword(userName, password);
        userDTO.setPhone(optional.get().getPhone());
        userDTO.setLastName(optional.get().getLastName());
        userDTO.setFirstName(optional.get().getFirstName());
        userDTO.setEmail(optional.get().getEmail());
        userDTO.setUserName(optional.get().getUsername());
        return userDTO;
    }
}
