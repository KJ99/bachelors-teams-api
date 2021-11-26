package pl.kj.bachelors.teams.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;

import java.util.Optional;

public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Integer> {
    Optional<TeamInvitation> findFirstByCode(String code);
    Optional<TeamInvitation> findFirstByToken(String token);
}
