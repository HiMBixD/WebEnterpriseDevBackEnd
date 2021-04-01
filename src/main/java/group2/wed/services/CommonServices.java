package group2.wed.services;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.otherComponent.AppResponseException;
import group2.wed.controllers.otherComponent.Message;
import group2.wed.controllers.um.request.*;
import group2.wed.entities.*;
import group2.wed.entities.dto.AssigmentDTO;
import group2.wed.entities.dto.UserDTO;
import group2.wed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommonServices {
    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DeadlineRepository deadlineRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserService userService;

    public List<Faculty> getAllFaculty() {
        return this.facultyRepository.findAll();
    }

    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<Object> searchAssignment(SearchAssignmentRequest request) {
        try {
            if (StringUtils.isEmpty(request)) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "request"));
            }
            List<Assignment> list = assignmentRepository.searchAssignmentByFaOrYearOrCreate_by(request.getFacultyId(), request.getDeadlineId(), request.getUsername());
            List<Object> objectList = list.stream().map(assignment -> new AssigmentDTO(assignment,
                    submissionRepository.countAllByAssignmentId(assignment.getAssignmentId()),
                    submissionRepository.countAllByStatusAndAssignmentId(1, assignment.getAssignmentId()))).collect(Collectors.toList());
            return objectList;
        }catch (Exception e){
            throw e;
        }
    }

    public Assignment createAssignment(CreateAssignmentRequest request) {
        try {
            if (StringUtils.isEmpty(request.getAssignName())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "assignName"));
            }
            if (StringUtils.isEmpty(request.getFacultyId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "facultyId"));
            }
            if (StringUtils.isEmpty(request.getDeadlineId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "deadlineId"));
            }
            Optional<Assignment> optional = assignmentRepository.findAssignmentByName(request.getAssignName());
            if (optional.isPresent()) {
                throw new AppResponseException(new Message(AppConstants.DUPLICATE, "assignName"));
            }
            Optional<Deadline> optionalDeadline = deadlineRepository.findById(request.getDeadlineId().intValue());
            if (optionalDeadline.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "deadlineId"));
            }
            UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Assignment assignment = new Assignment();
            assignment.setAssignmentName(request.getAssignName());
            assignment.setDescription(request.getDescription());
            assignment.setCreate_by(userDetails.getUsername());
            assignment.setDeadline(optionalDeadline.get());
            assignment.setFacultyId(request.getFacultyId());
            assignmentRepository.save(assignment);
            return assignment;
        }catch (Exception e){
            throw e;
        }
    }

    public Deadline setClosureDate(SetClosureDateRequest request) {
        try {
            if (StringUtils.isEmpty(request.getAction())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "action"));
            }
            if (StringUtils.isEmpty(request.getStartDate())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "startDate"));
            }
            if (StringUtils.isEmpty(request.getEndDate())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "endDate"));
            }
//            UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Deadline deadline = new Deadline();
            if (request.getAction().equals("create")) {
                deadline.setStartDate(request.getStartDate());
                deadline.setEndDate(request.getEndDate());
            } else if (request.getAction().equals("set")) {
                if (StringUtils.isEmpty(request.getId())) {
                    throw new AppResponseException(new Message(AppConstants.NOT_NULL, "id"));
                }
                Optional<Deadline> optionalDeadline =  deadlineRepository.findById(request.getId().intValue());
                if (optionalDeadline.isEmpty()) {
                    throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "id"));
                }
                deadline = optionalDeadline.get();
                deadline.setStartDate(request.getStartDate());
                deadline.setEndDate(request.getEndDate());
            } else {

            }
            deadlineRepository.save(deadline);
            return deadline;
        }catch (Exception e){
            throw e;
        }
    }

    public Submission postSubmission(PostSubmissionRequest request) throws Exception {
        try {
            if (StringUtils.isEmpty(request.getAssignmentId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "assignmentId"));
            }
            Assignment assignment = checkValidClosure(request.getAssignmentId());
            Optional<User> teacherOptional = userService.findByUsername(assignment.getCreate_by());
            if (teacherOptional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Assignment's Owner"));
            }
            UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Submission submission = new Submission();
//            submission.setYear(Long.parseLong(request.getYear()));
            submission.setAssignmentId(request.getAssignmentId());
            submission.setUsername(userDetails.getUsername());
            submission.setStatus(0);
            submission.setSubmissionDate(new Date());
            submissionRepository.save(submission);
            String mailContent = "<p><span style=\"font-size:14px\"><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"color:#000000\"><strong>Dear Mr/Mrs "+teacherOptional.get().getFirstName()+"</strong>,</span></span></span></p>" +
                    "<p><span style=\"font-size:14px\"><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"color:#000000\">There is an update to your assignment in <strong>"+assignment.getAssignmentName()+"</strong> by <strong>"+userDetails.getUsername()+"</strong>. Please provide a response in </span><span style=\"color:#c0392b\"><strong>14</strong></span><span style=\"color:#000000\"> days.</span></span></span></p>" +
                    "<p><span style=\"font-size:14px\"><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"color:#000000\">Best regards,</span></span></span></p>\n";
            String mailHeader = "New Submission on your Assignment";
            sendEmail(teacherOptional.get().getEmail(),mailContent,mailHeader);
            return submission;
        }catch (Exception e){
            throw e;
        }
    }

    public Submission selectSubmission(SelectSubmissionRequest request) throws Exception {
        try {
            if (StringUtils.isEmpty(request.getSubmissionId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "submissionId"));
            }
            if (request.getStatus() != 1 && request.getStatus()!= 2) {
                throw new AppResponseException(new Message(AppConstants.INVALID, "status must be 1 or 2"));
            }
            Optional<Submission> optionalSubmission = submissionRepository.findById(request.getSubmissionId());
            if (optionalSubmission.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "submissionId"));
            }
            Assignment assignment = checkValidClosure(optionalSubmission.get().getAssignmentId());
            Submission submission = optionalSubmission.get();
            submission.setStatus(request.getStatus());
            // status = 0 = no action yet;
            // status = 1 = selected;
            // status = 2 = deny + need fix;
            // status = 3 = has comment no selected
            submissionRepository.save(submission);
            Optional<User> userOptional = userService.findByUsername(submission.getUsername());
            if (userOptional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Submission owner"));
            }
            String mailContent = "<p><span style=\"font-size:14px\"><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"color:#000000\"><strong>Dear Mr/Mrs "+userOptional.get().getFirstName()+"</strong>,</span></span></span></p>" +
                    "<p><span style=\"font-size:14px\"><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"color:#000000\">Your submission for assignment: <strong>"+assignment.getAssignmentName()+"</strong> <span style=\"color:red\">has been selected</span>. Please login to get further information</span></span></span></p>" +
                    "<p><span style=\"font-size:14px\"><span style=\"font-family:Times New Roman,Times,serif\"><span style=\"color:#000000\">Best regards,</span></span></span></p>";
            String mailHeader = "Your submission upload on WED website is selected";
            sendEmail(userOptional.get().getEmail(), mailContent, mailHeader);
            return submission;
        }catch (Exception e){
            throw e;
        }
    }

    public List<Submission> searchSubmissions(SearchSubmissionRequest request) {
        try {
            List<Submission> list = submissionRepository.searchByUsernameOrStatusOrAssignmentId(request.getUsername(), request.getStatus(), request.getAssignmentId());
            return list;
        }catch (Exception e){
            throw e;
        }
    }

    public void addComment(AddCommentRequest request) {
        try {
            if (StringUtils.isEmpty(request.getSubmissionId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "submissionId"));
            }
            if (StringUtils.isEmpty(request.getContent())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "content"));
            }
            Optional<Submission> optionalSubmission = submissionRepository.findById(request.getSubmissionId().intValue());
            if (optionalSubmission.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "submissionId"));
            }
            Optional<Assignment> optionalAssignment =
                    assignmentRepository.findAssignmentById(optionalSubmission.get().getAssignmentId());
            if (optionalAssignment.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Assigment"));
            }
            UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            if (optionalAssignment.get().getCreate_by().equals(userDetails.getUsername())) {
                Submission submission = optionalSubmission.get();
                if (submission.getStatus() == 0) {
                    submission.setStatus(3);
                    submissionRepository.save(submission);
                }
            } else if (!optionalSubmission.get().getUsername().equals(userDetails.getUsername())) {
                throw new AppResponseException(new Message(AppConstants.NOT_ALLOWED, "You are not owner of this assigment or submission"));
            }

            Comment comment = new Comment();
            comment.setContent(request.getContent());
            comment.setSubmissionId(request.getSubmissionId().intValue());
            comment.setUsername(userDetails.getUsername());
            comment.setCreateDate(new Date());
            commentRepository.save(comment);
        }catch (Exception e){
            throw e;
        }
    }

    public List<Comment> getComments(GetCommentRequest request) {
        try {
            if (StringUtils.isEmpty(request.getSubmissionId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "submissionId"));
            }
            List<Comment> list = commentRepository.getAllBySubmissionId(request.getSubmissionId().intValue());
            return list;
        }catch (Exception e){
            throw e;
        }
    }

    public Deadline getDeadLine(GetDeadLineRequest request) {
        try {
            if (StringUtils.isEmpty(request.getDeadlineId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "deadlineId"));
            }
            Optional<Deadline> optionalDeadline = deadlineRepository.findById(request.getDeadlineId());
            if (optionalDeadline.isEmpty()){
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "deadlineId"));
            }
            return optionalDeadline.get();
        }catch (Exception e){
            throw e;
        }
    }

    public List<Deadline> getListDeadLine(GetListDeadLineRequest request) {
        try {
            List<Deadline> list = deadlineRepository.
                    findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(request.getDate().getFrom(), request.getDate().getTo());
            return list;
        }catch (Exception e){
            throw e;
        }
    }

    // common functions
    public Assignment checkValidClosure(Long assignmentId) {
        try {
            Optional<Assignment> optionalAssignment = assignmentRepository.findAssignmentById(assignmentId);
            if (optionalAssignment.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "assignmentId"));
            }
            Optional<Deadline> optionalDeadline = deadlineRepository.findById(optionalAssignment.get().getDeadline().getDeadlineId());
            Date now = new Date();
            if (optionalDeadline.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "deadlineId"));

            }
            if (now.after(optionalDeadline.get().getEndDate())) {
                throw new AppResponseException(new Message(AppConstants.INVALID, "This assignment is overdue"));
            }
            if (optionalDeadline.get().getStartDate().after(now)) {
                throw new AppResponseException(new Message(AppConstants.INVALID, "This assignment is not yet opened"));
            }
            return optionalAssignment.get();
        }catch (Exception e){
            throw e;
        }
    }

    public void sendEmail(String directEmail, String content, String header) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        boolean multipart = true;
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
        message.setContent(content, "text/html");
        helper.setTo(directEmail);
        helper.setSubject(header);
        this.emailSender.send(message);
    }
}
