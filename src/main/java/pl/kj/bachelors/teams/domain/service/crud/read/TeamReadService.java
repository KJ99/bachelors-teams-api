package pl.kj.bachelors.teams.domain.service.crud.read;

import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.result.TeamWithParticipationResult;

public interface TeamReadService extends ReadService<Team, Integer, TeamWithParticipationResult> {
}
