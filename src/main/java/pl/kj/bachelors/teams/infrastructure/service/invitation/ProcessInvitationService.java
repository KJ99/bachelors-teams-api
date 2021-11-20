package pl.kj.bachelors.teams.infrastructure.service.invitation;

import org.apache.commons.lang3.NotImplementedException;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;
import pl.kj.bachelors.teams.domain.service.invitation.InvitationProcessor;

public class ProcessInvitationService implements InvitationProcessor {
    @Override
    public TeamInvitation unwrap(String code) {
        throw new NotImplementedException();
    }

    @Override
    public void joinTeam(String uid, String invitationToken) {
        throw new NotImplementedException();
    }
}
