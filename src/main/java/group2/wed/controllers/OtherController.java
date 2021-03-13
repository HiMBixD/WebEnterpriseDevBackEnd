package group2.wed.controllers;

import group2.wed.constant.AppConstants;
import group2.wed.controllers.otherComponent.AppResponseException;
import group2.wed.controllers.otherComponent.Message;
import group2.wed.controllers.um.AdminController;
import group2.wed.controllers.um.request.CreateAssignmentRequest;
import group2.wed.controllers.um.request.PostSubmissionRequest;
import group2.wed.controllers.um.request.SearchSubmissionRequest;
import group2.wed.controllers.um.request.SelectSubmissionRequest;
import group2.wed.controllers.um.response.AppResponse;
import group2.wed.controllers.um.response.AppResponseFailure;
import group2.wed.controllers.um.response.AppResponseSuccess;
import group2.wed.services.CommonServices;
import group2.wed.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/")
public class OtherController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private CommonServices commonServices;

    @PostMapping("create-assignment")
    public AppResponse createAssignment(@RequestBody CreateAssignmentRequest request) {
        try {
            return new AppResponseSuccess(commonServices.createAssignment(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }

    @PostMapping("get-Faculties")
    public AppResponse getFaculties() {
        try {
            return new AppResponseSuccess(commonServices.getAllFaculty());
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }

    @PostMapping("student/post-submission")
    public AppResponse postSubmission(@RequestBody PostSubmissionRequest request) {
        try {
            return new AppResponseSuccess(commonServices.postSubmission(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }

    @PostMapping("select-submission")
    public AppResponse selectSubmission(@RequestBody SelectSubmissionRequest request) {
        try {
            return new AppResponseSuccess(commonServices.selectSubmission(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }

    @PostMapping("search-submission")
    public AppResponse searchSubmission(@RequestBody SearchSubmissionRequest request) {
        try {
            return new AppResponseSuccess(commonServices.searchSubmissions(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }
}
