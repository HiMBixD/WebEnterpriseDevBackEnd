package group2.wed.controllers.um.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditAssigmentRequest {
    private Long assignmentId;
    private String assignName;
    private String description;
    private Long deadlineId;
    private Long facultyId;
}
