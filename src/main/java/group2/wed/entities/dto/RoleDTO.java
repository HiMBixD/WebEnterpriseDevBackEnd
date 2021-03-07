package group2.wed.entities.dto;

import group2.wed.entities.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private String roleName;
    private String description;

    public RoleDTO(RoleEntity roleEntity) {
        this.roleName = roleEntity.getRoleName();
        this.description = roleEntity.getDescription();
    }
}
