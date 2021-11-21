package pl.kj.bachelors.teams.infrastructure.service.crud.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.service.crud.read.TeamMemberReadService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;

import java.util.Optional;

@Service
public class TeamMemberReadServiceImpl
    extends BaseReadService<TeamMember, Integer, TeamMemberRepository, TeamMember>
    implements TeamMemberReadService {

    private final TeamRepository teamRepository;

    @Autowired
    protected TeamMemberReadServiceImpl(TeamMemberRepository repository, TeamRepository teamRepository) {
        super(repository);
        this.teamRepository = teamRepository;
    }

    @Override
    protected TeamMember createResult(TeamMember source) {
        return source;
    }

    @Override
    public Page<TeamMember> readPagedByTeam(Integer teamId, Pageable query) throws ResourceNotFoundException {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);

        return this.repository.findByTeam(team, query);
    }

    @Override
    public Optional<TeamMember> readParticularByUserId(Integer teamId, String userId) throws ResourceNotFoundException {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);
        return this.repository.findFirstByTeamAndUserId(team, userId);
    }

}
