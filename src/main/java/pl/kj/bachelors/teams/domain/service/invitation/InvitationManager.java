package pl.kj.bachelors.teams.domain.service.invitation;

import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;

import java.util.concurrent.ExecutionException;

public interface InvitationManager {
    TeamInvitation open(int teamId) throws ResourceNotFoundException, ExecutionException, InterruptedException, AccessDeniedException;
    void close(String code) throws ResourceNotFoundException, AccessDeniedException;
}
