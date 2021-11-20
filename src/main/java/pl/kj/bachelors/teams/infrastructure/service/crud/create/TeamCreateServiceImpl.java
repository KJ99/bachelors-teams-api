package pl.kj.bachelors.teams.infrastructure.service.crud.create;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.CredentialsNotFoundException;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.service.ModelValidator;
import pl.kj.bachelors.teams.domain.service.crud.create.TeamCreateService;
import pl.kj.bachelors.teams.domain.service.user.UserProvider;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;

@Service
public class TeamCreateServiceImpl
        extends BaseEntityCreateService<Team, Integer, TeamRepository, TeamCreateModel>
        implements TeamCreateService {

    private final UserProvider userProvider;
    private final TeamMemberRepository teamMemberRepository;

    @Autowired
    public TeamCreateServiceImpl(
            ModelMapper modelMapper,
            TeamRepository repository,
            ModelValidator validator,
            UserProvider userProvider,
            TeamMemberRepository teamMemberRepository
    ) {
        super(modelMapper, repository, validator);
        this.userProvider = userProvider;
        this.teamMemberRepository = teamMemberRepository;
    }

    @Override
    protected void postCreate(Team team) throws Exception {
        String uid = this.userProvider.getCurrentUserId().orElseThrow(AccessDeniedException::new);
        TeamMember member = new TeamMember();
        member.setTeam(team);
        member.setUserId(uid);

        this.teamMemberRepository.save(member);
    }
}
