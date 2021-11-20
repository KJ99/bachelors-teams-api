package pl.kj.bachelors.teams.domain.service.invitation;

import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;

public interface InvitationProcessor {
    TeamInvitation unwrap(String code);
    void joinTeam(String uid, String invitationToken);
}
