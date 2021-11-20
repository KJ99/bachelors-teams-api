package pl.kj.bachelors.teams.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kj.bachelors.teams.domain.model.Role;
import pl.kj.bachelors.teams.domain.model.entity.TeamRole;

public interface TeamRoleRepository extends JpaRepository<TeamRole, Role> {
}
