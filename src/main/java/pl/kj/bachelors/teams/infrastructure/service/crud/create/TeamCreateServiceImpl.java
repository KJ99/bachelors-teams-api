package pl.kj.bachelors.teams.infrastructure.service.crud.create;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.model.extension.Role;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.domain.model.create.TeamMemberCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.service.ModelValidator;
import pl.kj.bachelors.teams.domain.service.crud.create.TeamCreateService;
import pl.kj.bachelors.teams.domain.service.crud.create.TeamMemberCreateService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;
import pl.kj.bachelors.teams.infrastructure.user.UserHandler;

import java.util.HashSet;
import java.util.Set;

@Service
public class TeamCreateServiceImpl
        extends BaseEntityCreateService<Team, Integer, TeamRepository, TeamCreateModel>
        implements TeamCreateService {

    private final TeamMemberCreateService teamMemberCreateService;

    @Autowired
    public TeamCreateServiceImpl(
            ModelMapper modelMapper,
            TeamRepository repository,
            ModelValidator validator,
            TeamMemberCreateService teamMemberCreateService) {
        super(modelMapper, repository, validator);
        this.teamMemberCreateService = teamMemberCreateService;
    }

    @Override
    protected void postCreate(Team team) throws Exception {
        String uid = UserHandler.getCurrentUserId().orElseThrow(AccessDeniedException::new);
        Set<Role> memberRoles = new HashSet<>();
        memberRoles.add(Role.OWNER);

        TeamMemberCreateModel memberCreateModel = new TeamMemberCreateModel();
        memberCreateModel.setTeamId(team.getId());
        memberCreateModel.setUserId(uid);
        memberCreateModel.setRoles(memberRoles);

        this.teamMemberCreateService.create(memberCreateModel, TeamMember.class);
    }
}
