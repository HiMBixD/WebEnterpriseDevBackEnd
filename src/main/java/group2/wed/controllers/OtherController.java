package group2.wed.controllers;

import group2.wed.controllers.otherComponent.AppResponseException;
import group2.wed.controllers.um.AdminController;
import group2.wed.controllers.um.request.*;
import group2.wed.controllers.um.response.AppResponse;
import group2.wed.controllers.um.response.AppResponseFailure;
import group2.wed.controllers.um.response.AppResponseSuccess;
import group2.wed.services.CommonServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/")
public class OtherController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

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

    @PostMapping("search-assignment")
    public AppResponse searchAssignment(@RequestBody SearchAssignmentRequest request) {
        try {
            return new AppResponseSuccess(commonServices.searchAssignment(request));
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

    @PostMapping("get-Roles")
    public AppResponse getRoles() {
        try {
            return new AppResponseSuccess(commonServices.getAllRoles());
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

    @PostMapping("add-comment")
    public AppResponse addComment(@RequestBody AddCommentRequest request) {
        try {
            commonServices.addComment(request);
            return new AppResponseSuccess();
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }

    @PostMapping("get-comment")
    public AppResponse getCommentBySubmission(@RequestBody GetCommentRequest request) {
        try {
            return new AppResponseSuccess(commonServices.getComments(request));
        } catch (AppResponseException exception) {
            return new AppResponseFailure(exception.responseMessage);
        }
    }
}
