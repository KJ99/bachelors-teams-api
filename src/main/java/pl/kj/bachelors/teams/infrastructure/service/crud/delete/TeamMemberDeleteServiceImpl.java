package pl.kj.bachelors.teams.infrastructure.service.crud.delete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.extension.Role;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.service.crud.delete.TeamMemberDeleteService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;

@Service
public class TeamMemberDeleteServiceImpl
        extends BaseEntityDeleteService<TeamMember, Integer, TeamMemberRepository>
        implements TeamMemberDeleteService {
    private final TeamRepository teamRepository;

    @Autowired
    protected TeamMemberDeleteServiceImpl(TeamMemberRepository repository, TeamRepository teamRepository) {
        super(repository);
        this.teamRepository = teamRepository;
    }

    @Override
    @Transactional(rollbackFor = { ResourceNotFoundException.class })
    public void deleteByTeamAndUserId(Integer teamId, String userId) throws Exception {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);
        TeamMember member = this.repository
                .findFirstByTeamAndUserId(team, userId)
                .orElseThrow(ResourceNotFoundException::new);
        this.preDelete(member);
        this.repository.delete(member);
    }

    @Override
    protected void preDelete(TeamMember entity) throws Exception {
        if(entity.getRoles().stream().anyMatch(teamRole -> teamRole.getCode().equals(Role.OWNER))) {
            throw new AccessDeniedException();
        }
    }
}
