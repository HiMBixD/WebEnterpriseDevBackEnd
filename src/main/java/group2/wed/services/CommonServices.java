package group2.wed.services;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.otherComponent.AppResponseException;
import group2.wed.controllers.otherComponent.Message;
import group2.wed.controllers.um.request.*;
import group2.wed.entities.*;
import group2.wed.entities.dto.UserDTO;
import group2.wed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommonServices {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DeadlineRepository deadlineRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    public List<Faculty> getAllFaculty() {
        return this.facultyRepository.findAll();
    }

    public Assignment createAssignment(CreateAssignmentRequest request) {
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
            assignment.setDeadlineId(LocalDate.now().getYear());
            assignment.setFacultyId(request.getFacultyId());
            assignmentRepository.save(assignment);
            return assignment;
        }catch (Exception e){
            throw e;
        }
    }

    public Date setClosureDate(SetClosureDateRequest request) {
        try {
            if (StringUtils.isEmpty(request.getYear())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "year"));
            }
            if (StringUtils.isEmpty(request.getClosureDate())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "closureDate"));
            }
//            UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Deadline deadline = new Deadline();
            deadline.setYear(Long.parseLong(request.getYear()));
            deadline.setClosureDate(request.getClosureDate());
            deadlineRepository.save(deadline);
            return deadline.getClosureDate();
        }catch (Exception e){
            throw e;
        }
    }

    public Submission postSubmission(PostSubmissionRequest request) {
        try {
            if (StringUtils.isEmpty(request.getAssignmentId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "assignmentId"));
            }
            checkValidClosure(request.getAssignmentId());
            UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Submission submission = new Submission();
//            submission.setYear(Long.parseLong(request.getYear()));
            submission.setAssignmentId(request.getAssignmentId());
            submission.setUsername(userDetails.getUsername());
            submission.setSubmissionDate(new Date());
            submissionRepository.save(submission);
            return submission;
        }catch (Exception e){
            throw e;
        }
    }

    public Submission selectSubmission(SelectSubmissionRequest request) {
        try {
            if (StringUtils.isEmpty(request.getSubmissionId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "submissionId"));
            }
            Optional<Submission> optionalSubmission = submissionRepository.findById(request.getSubmissionId());
            if (optionalSubmission.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "submissionId"));
            }
            checkValidClosure(optionalSubmission.get().getAssignmentId());
            Submission submission = optionalSubmission.get();
            submission.setStatus(1);
            submissionRepository.save(submission);
            return submission;
        }catch (Exception e){
            throw e;
        }
    }

    public List<Submission> searchSubmissions(SearchSubmissionRequest request) {
        try {
            List<Submission> list = submissionRepository.searchByUsernameOrStatus(request.getUsername(), request.getStatus());
            return list;
        }catch (Exception e){
            throw e;
        }
    }


    // common functions
    public void checkValidClosure(Long assignmentId) {
        try {
            Optional<Assignment> optionalAssignment = assignmentRepository.findAssignmentById(assignmentId);
            if (optionalAssignment.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "assignmentId"));
            }
            Optional<Deadline> optionalDeadline = deadlineRepository.findDeadlineById(Long.valueOf(optionalAssignment.get().getDeadlineId()));
            Date now = new Date();
            if (optionalDeadline.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "deadlineId"));

            }
            if (now.after(optionalDeadline.get().getClosureDate())) {
                throw new AppResponseException(new Message(AppConstants.INVALID, "This assignment is overdue"));
            }
        }catch (Exception e){
            throw e;
        }
    }
}
