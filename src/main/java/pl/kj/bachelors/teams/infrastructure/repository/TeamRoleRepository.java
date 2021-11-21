package pl.kj.bachelors.teams.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kj.bachelors.teams.domain.model.Role;
import pl.kj.bachelors.teams.domain.model.entity.TeamRole;

import java.util.List;

public interface TeamRoleRepository extends JpaRepository<TeamRole, Role> {
    @Query("select r from TeamRole r where r.code not in (:exclusions)")
    List<TeamRole> findExcluding(@Param("exclusions") List<Role> exclusions);
}
