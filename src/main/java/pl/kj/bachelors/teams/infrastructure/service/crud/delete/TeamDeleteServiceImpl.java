package pl.kj.bachelors.teams.infrastructure.service.crud.delete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.service.crud.delete.TeamDeleteService;
import pl.kj.bachelors.teams.domain.service.crud.delete.TeamMemberDeleteService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;

@Service
public class TeamDeleteServiceImpl
        extends BaseEntityDeleteService<Team, Integer, TeamRepository>
        implements TeamDeleteService {
    @Autowired
    protected TeamDeleteServiceImpl(TeamRepository repository) {
        super(repository);
    }
}
