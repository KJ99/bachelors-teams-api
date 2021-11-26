package pl.kj.bachelors.teams.infrastructure.service.security.voter;

import org.springframework.stereotype.Component;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.extension.AccessVote;
import pl.kj.bachelors.teams.domain.model.extension.Role;
import pl.kj.bachelors.teams.domain.model.extension.action.TeamMemberAction;

import java.util.Arrays;

@Component
public class TeamMemberVoter extends BaseTeamVoter<TeamMemberAction> {
    @Override
    protected AccessVote voteInternal(Team subject, TeamMemberAction action, TeamMember member) {
        AccessVote vote;
        switch (action) {
            case READ:
                vote = AccessVote.ALLOW;
                break;
            case UPDATE:
            case DELETE:
                vote = this.hasRole(member, Arrays.asList(Role.ADMIN, Role.OWNER)) ? AccessVote.ALLOW : AccessVote.DENY;
                break;
            default:
                vote = AccessVote.OMIT;
                break;
        }
        return vote;
    }

    @Override
    protected TeamMemberAction[] getSupportedActions() {
        return TeamMemberAction.values();
    }
}
