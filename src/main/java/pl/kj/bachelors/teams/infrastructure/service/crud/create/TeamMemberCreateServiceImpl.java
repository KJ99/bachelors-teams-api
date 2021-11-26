package pl.kj.bachelors.teams.infrastructure.service.crud.create;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.model.extension.Role;
import pl.kj.bachelors.teams.domain.model.create.TeamMemberCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.service.ModelValidator;
import pl.kj.bachelors.teams.domain.service.crud.create.TeamMemberCreateService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;

@Service
public class TeamMemberCreateServiceImpl
        extends BaseEntityCreateService<TeamMember, Integer, TeamMemberRepository, TeamMemberCreateModel>
        implements TeamMemberCreateService {
    @Autowired
    protected TeamMemberCreateServiceImpl(ModelMapper modelMapper, TeamMemberRepository repository, ModelValidator validator) {
        super(modelMapper, repository, validator);
    }

    @Override
    protected void preCreate(TeamMemberCreateModel model) {
        model.getRoles().add(Role.TEAM_MEMBER);
    }
}
