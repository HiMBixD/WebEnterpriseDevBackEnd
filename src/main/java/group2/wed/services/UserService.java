package group2.wed.services;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.other.AppResponseException;
import group2.wed.controllers.other.Message;
import group2.wed.controllers.um.request.CreateUserRequest;
import group2.wed.controllers.um.request.GetUserInfoRequest;
import group2.wed.controllers.um.request.UpdateUserInfoRequest;
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

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserDTO createUser(CreateUserRequest request) {
        try {
            Optional<RoleEntity> optional = roleRepository.findByRoleId(request.getRoleId());
            if (!optional.isPresent()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "RoleId"));
            }
            if (findByUsername(request.getUsername()).isPresent()) {
                throw new AppResponseException(new Message(AppConstants.EXISTED, "Username"));
            }
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRoleEntity(optional.get());
            userRepository.save(user);
            return new UserDTO(user);
        }catch (Exception e){
            throw e;
        }
    }

    public UserDTO getUserInfo(GetUserInfoRequest request) {
        try {
            Optional<User> optional = findByUsername(request.getUsername());
            if (findByUsername(request.getUsername()).isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
            }
            return new UserDTO(optional.get());
        }catch (Exception e){
            throw e;
        }
    }

    public UserDTO updateUserInfo(UpdateUserInfoRequest request, Boolean allowRoleChange) {
        try {
            if (request.getUsername().isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "Username"));
            }
            Optional<User> optionalUser = findByUsername(request.getUsername());
            if (optionalUser.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Username"));
            }

            User user = optionalUser.get();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPhone(request.getPhone());

            if (request.getRoleId() != null && allowRoleChange) {
                Optional<RoleEntity> optionalRole = roleRepository.findByRoleId(request.getRoleId());
                if (optionalRole.isEmpty()) {
                    throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "RoleId"));
                }
                user.setRoleEntity(optionalRole.get());
            }
            userRepository.save(user);
            return new UserDTO(user);
        }catch (Exception e){
            throw e;
        }
    }

    public UserDTO findByUsernameAndPassword(String username, String password) {
        Optional<User> optionalUser = findByUsername(username);
        if (optionalUser.isPresent()) {
            if (passwordEncoder.matches(password, optionalUser.get().getPassword())) {
                return new UserDTO(optionalUser.get());
            }
        }
        return null;
    }
}
