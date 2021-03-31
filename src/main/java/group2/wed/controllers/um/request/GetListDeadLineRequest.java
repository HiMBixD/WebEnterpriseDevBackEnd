package group2.wed.controllers.um.request;

import group2.wed.controllers.otherComponent.DateObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetListDeadLineRequest {
    private DateObject date;
}
