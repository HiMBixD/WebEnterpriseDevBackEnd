package group2.wed.entities.dto;

import group2.wed.entities.Assignment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssigmentDTO {
    private Assignment assignment;
    private Integer totalSub;
    private Integer selectedSub;

    public AssigmentDTO(Assignment assignment, Integer totalSub, Integer selectedSub) {
        this.assignment = assignment;
        this.totalSub = totalSub;
        this.selectedSub = selectedSub;
    }
}
