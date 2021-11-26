package pl.kj.bachelors.teams.domain.service.crud.create;

import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;

public interface TeamCreateService extends CreateService<Team, TeamCreateModel>{
}
