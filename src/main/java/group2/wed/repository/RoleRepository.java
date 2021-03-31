package group2.wed.repository;

import group2.wed.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    @Query("from RoleEntity r where r.roleId = :role_id")
    Optional<RoleEntity> findByRoleId(@Param("role_id") Long roleId);
}
