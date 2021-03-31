package group2.wed.controllers.um.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssignmentRequest {
    private String assignName;
    private String description;
    private Long facultyId;
}
