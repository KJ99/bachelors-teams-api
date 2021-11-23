package pl.kj.bachelors.teams.infrastructure.service.crud.read;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.result.TeamWithParticipationResult;
import pl.kj.bachelors.teams.domain.service.crud.read.TeamReadService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;
import pl.kj.bachelors.teams.infrastructure.user.RequestHandler;

import java.util.Optional;

@Service
public class TeamReadServiceImpl
        extends BaseReadService<Team, Integer, TeamRepository, TeamWithParticipationResult> implements TeamReadService {
    private final TeamMemberRepository memberRepository;
    @Autowired
    protected TeamReadServiceImpl(
            TeamRepository repository,
            TeamMemberRepository memberRepository
    ) {
        super(repository);
        this.memberRepository = memberRepository;
    }

    @Override
    public Page<TeamWithParticipationResult> readPaged(Pageable query) {
        String uid = RequestHandler.getCurrentUserId().orElse("");
        Page<Team> teamsPage = repository.findByUserId(uid, query);

        return teamsPage.map(this::createResult);
    }

    @Override
    public Optional<TeamWithParticipationResult> readParticular(Integer identity) {
        String uid = RequestHandler.getCurrentUserId().orElse("");
        Optional<Team> team = this.repository.findByIdWithParticipation(identity, uid);

        return team.map(this::createResult);
    }

    @Override
    protected TeamWithParticipationResult createResult(Team team) {
        String uid = RequestHandler.getCurrentUserId().orElse("");
        TeamMember member = this.memberRepository
                .findFirstByTeamAndUserId(team, uid)
                .orElse(new TeamMember());
        var result = new TeamWithParticipationResult();
        result.setTeam(team);
        result.setMember(member);

        return result;
    }
}
