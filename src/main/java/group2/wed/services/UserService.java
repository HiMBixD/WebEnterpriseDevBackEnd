package group2.wed.services;

import group2.wed.controllers.um.request.CreateUserRequest;
import group2.wed.entities.RoleEntity;
import group2.wed.entities.User;
import group2.wed.entities.dto.UserDTO;
import group2.wed.repository.RoleRepository;
import group2.wed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByLogin(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean createUser(CreateUserRequest request) throws Exception {
        try {
            Optional<RoleEntity> optional = roleRepository.findByRoleId(request.getRoleId());
            if (!optional.isPresent()) {
                throw new Exception();
            }
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRoleEntity(optional.get());
            userRepository.save(user);
        }catch (Exception e){
            throw e;
        }
        return true;
    }

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

    public UserDTO findByUsernameAndPassword(String username, String password) {
        Optional<User> optionalUser = findByLogin(username);
        if (optionalUser.isPresent()) {
            if (passwordEncoder.matches(password, optionalUser.get().getPassword())) {
                return new UserDTO(optionalUser.get());
            }
        }
        return null;
    }
}
