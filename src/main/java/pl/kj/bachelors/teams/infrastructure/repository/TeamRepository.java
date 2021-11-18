package pl.kj.bachelors.teams.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kj.bachelors.teams.domain.model.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Integer> {
}
