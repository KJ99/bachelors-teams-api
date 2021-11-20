package pl.kj.bachelors.teams.domain.service.invitation;

import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;

public interface InvitationProcessor {
    TeamInvitation unwrap(String code) throws ResourceNotFoundException, AccessDeniedException;
    void joinTeam(String uid, String invitationToken);
}
