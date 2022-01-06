package pl.kj.bachelors.teams.infrastructure.service.crud.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.remote.UserProfile;
import pl.kj.bachelors.teams.domain.model.result.TeamMemberWithProfileResult;
import pl.kj.bachelors.teams.domain.service.crud.read.TeamMemberReadService;
import pl.kj.bachelors.teams.domain.service.user.ProfileProvider;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamMemberReadServiceImpl
    extends BaseReadService<TeamMember, Integer, TeamMemberRepository, TeamMemberWithProfileResult>
    implements TeamMemberReadService {

    private final TeamRepository teamRepository;
    private final ProfileProvider profileProvider;

    @Autowired
    protected TeamMemberReadServiceImpl(
            TeamMemberRepository repository,
            TeamRepository teamRepository,
            ProfileProvider profileProvider
    ) {
        super(repository);
        this.teamRepository = teamRepository;
        this.profileProvider = profileProvider;
    }

    @Override
    protected TeamMemberWithProfileResult createResult(TeamMember source) {
        UserProfile defaultProfile = new UserProfile();
        defaultProfile.setId(source.getUserId());

        UserProfile profile = this.profileProvider.get(source.getUserId()).orElse(defaultProfile);
        TeamMemberWithProfileResult result = new TeamMemberWithProfileResult();
        result.setMember(source);
        result.setProfile(profile);

        return result;
    }

    @Override
    public Page<TeamMemberWithProfileResult> readPagedByTeam(Integer teamId, Pageable query) throws ResourceNotFoundException {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);

        Page<TeamMember> membersPage = this.repository.findByTeam(team, query);

        return membersPage.map(this::createResult);
    }

    @Override
    public List<TeamMemberWithProfileResult> readByTeam(Integer teamId) throws ResourceNotFoundException {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);

        List<TeamMember> members = this.repository.findByTeam(team);

        return members.stream().map(this::createResult).collect(Collectors.toList());
    }

    @Override
    public Optional<TeamMemberWithProfileResult> readParticularByUserId(Integer teamId, String userId) throws ResourceNotFoundException {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);
        return this.repository.findFirstByTeamAndUserId(team, userId).map(this::createResult);
    }

}
