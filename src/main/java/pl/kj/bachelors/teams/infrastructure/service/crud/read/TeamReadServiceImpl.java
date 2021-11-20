package pl.kj.bachelors.teams.infrastructure.service.crud.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.service.crud.read.TeamReadService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;

@Service
public class TeamReadServiceImpl extends BaseReadService<Team, Integer, TeamRepository> implements TeamReadService {
    @Autowired
    protected TeamReadServiceImpl(TeamRepository repository) {
        super(repository);
    }
}
