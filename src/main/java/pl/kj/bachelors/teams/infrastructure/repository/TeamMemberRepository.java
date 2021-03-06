package pl.kj.bachelors.teams.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Integer> {
    Optional<TeamMember> findFirstByTeamAndUserId(Team team, String userId);
    Page<TeamMember> findByTeam(Team team, Pageable pageable);
    List<TeamMember> findByTeam(Team team);
}
