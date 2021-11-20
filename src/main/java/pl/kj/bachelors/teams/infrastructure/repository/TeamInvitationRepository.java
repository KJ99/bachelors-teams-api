package pl.kj.bachelors.teams.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;

public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Integer> {
}
