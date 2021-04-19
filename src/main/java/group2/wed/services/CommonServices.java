package group2.wed.services;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.otherComponent.AppResponseException;
import group2.wed.controllers.otherComponent.Message;
import group2.wed.controllers.um.request.*;
import group2.wed.entities.*;
import group2.wed.entities.dto.AssigmentDTO;
import group2.wed.entities.dto.SubmissionDTO;
import group2.wed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommonServices {
    @Value("$(db.username)")
    private String dbUsername;
    @Value("$(db.password)")
    private String dbPassword;
    @Value("$(db.schema)")
    private String dbName;
    @Value("$(db.port)")
    private String dbPort;
    @Value("$(db.hostname)")
    private String dbHostName;
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
            List<Submission> submissionList = submissionRepository.findAll();
            List<Assignment> list = assignmentRepository.searchAssignmentByFaOrYearOrCreate_by(request.getFacultyId(), request.getDeadlineId(), request.getUsername());
            return list.stream().map(assignment -> {
                 int all= (int) submissionList.stream().filter(val -> val.getAssignmentId().equals(assignment.getAssignmentId())).count();
                 int selected= (int) submissionList.stream().filter(c -> c.getStatus() == 1 && c.getAssignmentId().equals(assignment.getAssignmentId())).count();
                 return new AssigmentDTO(assignment,all,selected);
            }).collect(Collectors.toList());
        }catch (Exception e){
            throw e;
        }
    }

    public Assignment getAssignment(GetAssigmentByIdRequest request) {
        try {
            if (StringUtils.isEmpty(request.getAssignmentId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "assignmentId"));
            }
            Optional<Assignment> optional = assignmentRepository.findAssignmentById(request.getAssignmentId());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "assignmentId"));
            }
            UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            return optional.get();
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

    public Assignment editAssignment(EditAssigmentRequest request) {
        try {
            if (StringUtils.isEmpty(request.getFacultyId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "FacultyId"));
            }
            if (StringUtils.isEmpty(request.getAssignmentId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "assignmentId"));
            }
            if (StringUtils.isEmpty(request.getAssignName())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "AssignName"));
            }
            Optional<Assignment> optional = assignmentRepository.findAssignmentById(request.getAssignmentId());
            if (optional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "assignmentId"));
            }
            Assignment assignment = optional.get();
            if (!StringUtils.isEmpty(request.getDeadlineId())) {
                Optional<Deadline> optionalDeadline = deadlineRepository.findById(request.getDeadlineId().intValue());
                if (optionalDeadline.isEmpty()) {
                    throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "deadlineId"));
                }
                assignment.setDeadline(optionalDeadline.get());
            }
            assignment.setFacultyId(request.getFacultyId());
            assignment.setAssignmentName(request.getAssignName());
            assignment.setDescription(request.getDescription());
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
            UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            List<Submission> listSubmission = submissionRepository.
                    searchByUsernameOrStatusOrAssignmentId(userDetails.getUsername(), null, request.getAssignmentId());
            Assignment assignment;
            if (listSubmission.isEmpty()) {
                assignment = checkValidClosure(request.getAssignmentId(), false);
            } else {
                assignment = checkValidClosure(request.getAssignmentId(), true);
            }
            Optional<User> teacherOptional = userService.findByUsername(assignment.getCreate_by());
            if (teacherOptional.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "Assignment's Owner"));
            }
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

            Optional<Assignment> optionalAssignment = assignmentRepository.findAssignmentById(optionalSubmission.get().getAssignmentId());
            if (optionalAssignment.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "assignmentId"));
            }
            Assignment assignment = optionalAssignment.get();
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

    public List<SubmissionDTO> searchSubmissions(SearchSubmissionRequest request) {
        try {
            List<Submission> list = submissionRepository.searchByUsernameOrStatusOrAssignmentId(request.getUsername(), request.getStatus(), request.getAssignmentId());
            List<Comment> commentList = commentRepository.findAllBySubmissionIdIn(
                    list.stream().map(Submission::getSubmissionId).collect(Collectors.toList())
            );
            List<SubmissionDTO> submissionDTOS = list.stream().map(submission -> new SubmissionDTO(submission, (int)commentList.stream().
                    filter(val -> val.getSubmissionId().equals(submission.getSubmissionId())).count())).collect(Collectors.toList());
            return submissionDTOS;
        }catch (Exception e){
            throw e;
        }
    }

    public List<Submission> countSubmissions(CountSubmisByFalcutyRequest request) {
        try {
            if (StringUtils.isEmpty(request.getFacultyId())) {
                throw new AppResponseException(new Message(AppConstants.NOT_NULL, "facultyId"));
            }
            List<Assignment> listAssignments = assignmentRepository.findAllByFacultyId(request.getFacultyId());
            return submissionRepository.findAllByAssignmentIdIn(
                    listAssignments.stream().map(Assignment::getAssignmentId)
                            .collect(Collectors.toList())
            );
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
            Optional<User> optionalUser = userService.findByUsername(userDetails.getUsername());
            if (optionalUser.isEmpty()) {
                throw new AppResponseException(new Message(AppConstants.NOT_FOUND, "username"));
            }
            if (optionalAssignment.get().getFacultyId().equals(optionalUser.get().getFacultyId()) && optionalUser.get().getRoleEntity().getRoleId() == 2) {
                Submission submission = optionalSubmission.get();
                if (submission.getStatus() == 0) {
                    submission.setStatus(3);
                    submissionRepository.save(submission);
                }
            } else if (!optionalSubmission.get().getUsername().equals(optionalUser.get().getUsername())) {
                throw new AppResponseException(new Message(AppConstants.NOT_ALLOWED, "You are not owner of this submission or coordinator from valid faculty"));
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
    public Assignment checkValidClosure(Long assignmentId, Boolean isSubmissionExisted) {
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

            int addCondition = 0;
            String mess = "Create failed!";
            if (isSubmissionExisted) {
                addCondition = 14;
                mess= "14 days passed! Update failed!";
            }

            if (now.after(new Date(optionalDeadline.get().getEndDate().getTime() + addCondition*24*60*60*1000))) {
                throw new AppResponseException(new Message(AppConstants.INVALID, mess + "This assignment is overdue"));
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

    public boolean backupSQL()
            throws IOException, InterruptedException {
        try {
            final List<String> baseCmds = new ArrayList<String>();
            baseCmds.add("C:\\Program Files\\PostgreSQL\\13\\bin\\pg_dump");
            baseCmds.add("-h");
            baseCmds.add(dbHostName);
            baseCmds.add("-p");
            baseCmds.add(dbPort);
            baseCmds.add("-U");
            baseCmds.add(dbUsername);
            baseCmds.add("-b");
            baseCmds.add("-v");
            baseCmds.add("-f");
            baseCmds.add("E:\\programer\\Topup\\wed\\WebEnterpriseDevBackEnd\\uploads\\" +
                    "backup.sql");
            baseCmds.add(dbName);
            final ProcessBuilder pb = new ProcessBuilder(
                    "C:\\Program Files\\PostgreSQL\\13\\bin\\pg_dump",
                    "--host=" + dbHostName,
                    "--port=" + dbPort,
                    "--username=" + dbUsername,
                    "--password=" + dbPassword,
                    "--format=custom",
                    "--blobs",
                    "--verbose", "--file=D:\\service_station_backup.sql", dbName);

//            // Set the password
//            final Map<String, String> env = pb.environment();
//            env.put("PGPASSWORD", dbPassword);
            Process process = pb.start();
            final BufferedReader r = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            String line = r.readLine();
            while (line != null) {
                System.err.println(line);
                line = r.readLine();
            }
            r.close();

            final int dcertExitCode = process.waitFor();
            return true;
        }catch (Exception e){
            throw e;
        }
    }
    private final Path root = Paths.get(AppConstants.ROOT_FOLDER);

}
