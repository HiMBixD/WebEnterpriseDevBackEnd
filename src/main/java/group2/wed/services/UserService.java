package group2.wed.services;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.otherComponent.AppResponseException;
import group2.wed.controllers.otherComponent.Message;
import group2.wed.controllers.um.request.CreateUserRequest;
import group2.wed.controllers.um.request.GetUserInfoRequest;
import group2.wed.controllers.um.request.SearchUserRequest;
import group2.wed.controllers.um.request.UpdateUserInfoRequest;
import group2.wed.entities.Faculty;
import group2.wed.entities.RoleEntity;
import group2.wed.entities.User;
import group2.wed.entities.dto.UserDTO;
import group2.wed.repository.FacultyRepository;
import group2.wed.repository.RoleRepository;
import group2.wed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserDTO createUser(CreateUserRequest request) {
        try {
            if (StringUtils.isEmpty(request.getUsername())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "username"));
            }
            if (StringUtils.isEmpty(request.getPassword())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "password"));
            }
            if (StringUtils.isEmpty(request.getRoleId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "roleId"));
            }
            if (StringUtils.isEmpty(request.getEmail())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "email"));
            }
            if (StringUtils.isEmpty(request.getFacultyId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "facultyId"));
            }

            Optional<RoleEntity> optional = roleRepository.findByRoleId(request.getRoleId());
            if (!optional.isPresent()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "RoleId"));
            }
            Optional<Faculty> optionalFaculty = facultyRepository.findFaById(request.getFacultyId());

            if (!optionalFaculty.isPresent()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "FacultyId"));
            }
            if (findByUsername(request.getUsername()).isPresent()) {
                throw new AppResponseException(new Message(AppConstants.EXISTED, "Username"));
            }
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPhone(request.getPhone());
            user.setEmail(request.getEmail());
            user.setFacultyId(optionalFaculty.get().getFacultyId());
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

    public List<UserDTO> searchUsersInfo(SearchUserRequest request) {
        try {
            List<User> users = userRepository.searchByUsername(request.getUsername());
            return users.stream().map(UserDTO::new).collect(Collectors.toList());
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
            if (allowRoleChange) {
                user.setEmail(request.getEmail());
                user.setFacultyId(request.getFacultyId());
            }
            user.setPhone(request.getPhone());
            user.setLastName(request.getLastName());
            user.setFirstName(request.getFirstName());

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
