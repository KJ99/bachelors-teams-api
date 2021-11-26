package pl.kj.bachelors.teams.infrastructure.service.security.voter;

import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.extension.AccessVote;
import pl.kj.bachelors.teams.domain.model.extension.Role;

import java.util.List;
import java.util.Optional;

public abstract class BaseTeamVoter<A> extends BaseVoter<Team, A> {
    @Override
    protected AccessVote voteInternal(Team subject, A action, String userId) {
        Optional<TeamMember> member = subject
                .getMembers()
                .stream()
                .filter(tm -> tm.getUserId().equals(userId))
                .findFirst();
        return member.isPresent() ? this.voteInternal(subject, action, member.get()) : AccessVote.DENY;
    }

    protected boolean hasRole(TeamMember member, Role role) {
        return member.getRoles().stream().anyMatch(teamRole -> teamRole.getCode().equals(role));
    }

    protected boolean hasRole(TeamMember member, List<Role> roles) {
        return member.getRoles().stream().anyMatch(teamRole -> roles.contains(teamRole.getCode()));
    }

    protected abstract AccessVote voteInternal(Team subject, A action, TeamMember member);
}
