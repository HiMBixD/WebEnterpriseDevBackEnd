package group2.wed.controllers.um.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchSubmissionRequest {
    private String username;
    private Long assignmentId;
    private Integer status;
    // status = 0 = no action yet;
    // status = 1 = selected;
    // status = 2 = deny + need fix;
    // status = 3 = has comment no selected
}
