package pl.kj.bachelors.teams.infrastructure.service.invitation;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;
import pl.kj.bachelors.teams.domain.service.invitation.InvitationProcessor;
import pl.kj.bachelors.teams.infrastructure.repository.TeamInvitationRepository;

import java.util.Calendar;

@Service
public class ProcessInvitationService implements InvitationProcessor {
    private final TeamInvitationRepository invitationRepository;

    @Autowired
    public ProcessInvitationService(TeamInvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    @Override
    public TeamInvitation unwrap(String code) throws ResourceNotFoundException, AccessDeniedException {
        TeamInvitation invitation = this.invitationRepository
                .findFirstByCode(code)
                .orElseThrow(ResourceNotFoundException::new);
        if(invitation.getExpiresAt().getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
            throw new AccessDeniedException("Invitation expired!");
        }

        return invitation;
    }

    @Override
    public void joinTeam(String uid, String invitationToken) {
        throw new NotImplementedException();
    }
}
