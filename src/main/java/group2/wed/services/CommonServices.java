package group2.wed.services;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.otherComponent.AppResponseException;
import group2.wed.controllers.otherComponent.Message;
import group2.wed.controllers.um.request.CreateAssignmentRequest;
import group2.wed.entities.Assignment;
import group2.wed.entities.Faculty;
import group2.wed.entities.RoleEntity;
import group2.wed.entities.User;
import group2.wed.entities.dto.UserDTO;
import group2.wed.repository.AssignmentRepository;
import group2.wed.repository.FacultyRepository;
import group2.wed.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommonServices {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    public List<Faculty> getAllFaculty() {
        return this.facultyRepository.findAll();
    }

    public boolean createAssignment(CreateAssignmentRequest request) {
        try {
            if (StringUtils.isEmpty(request.getAssignName())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "assignName"));
            }
            if (StringUtils.isEmpty(request.getFacultyId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "facultyId"));
            }
            UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Assignment assignment = new Assignment();
            assignment.setAssignmentName(request.getAssignName());
            assignment.setCreate_by(userDetails.getUsername());
            assignment.setYear(LocalDate.now().getYear());
            assignment.setFacultyId(request.getFacultyId());
            assignmentRepository.save(assignment);
            return true;
        }catch (Exception e){
            throw e;
        }
    }
}
