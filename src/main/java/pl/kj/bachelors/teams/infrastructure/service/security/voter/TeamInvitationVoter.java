package pl.kj.bachelors.teams.infrastructure.service.security.voter;

import org.springframework.stereotype.Component;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.extension.AccessVote;
import pl.kj.bachelors.teams.domain.model.extension.Role;
import pl.kj.bachelors.teams.domain.model.extension.action.TeamInvitationActions;

import java.util.Arrays;

@Component
public class TeamInvitationVoter extends BaseTeamVoter<TeamInvitationActions> {
    @Override
    protected AccessVote voteInternal(Team subject, TeamInvitationActions action, TeamMember member) {
        return this.hasRole(member, Arrays.asList(Role.OWNER, Role.ADMIN)) ? AccessVote.ALLOW : AccessVote.DENY;
    }

    @Override
    protected TeamInvitationActions[] getSupportedActions() {
        return new TeamInvitationActions[] {
                TeamInvitationActions.CREATE,
                TeamInvitationActions.CLOSE
        };
    }
}
