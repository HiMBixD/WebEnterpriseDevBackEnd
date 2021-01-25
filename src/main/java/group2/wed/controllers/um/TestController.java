package group2.wed.controllers.um;

import group2.wed.controllers.um.request.GetUserTestRequest;
import group2.wed.controllers.um.response.GetUserTestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    @PostMapping("/get/detail")
    public GetUserTestResponse getDetail(@RequestBody GetUserTestRequest request) {
        try {
            GetUserTestResponse response = apiAllowService.getDetail(request.getBody());
            return response;

        }  catch (Exception e) {
            return ResponseUtil.generateErrorResponse(e);
        }
    }
}
