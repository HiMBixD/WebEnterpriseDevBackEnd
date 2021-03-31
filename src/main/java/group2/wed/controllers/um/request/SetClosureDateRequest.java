package group2.wed.controllers.um.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetClosureDateRequest {
    private String action;
    private Long id;
    private Date startDate;
    private Date endDate;
}
