package pl.kj.bachelors.teams.infrastructure.service.invitation;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.teams.domain.config.TeamInvitationConfig;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;
import pl.kj.bachelors.teams.domain.service.invitation.InvitationManager;
import pl.kj.bachelors.teams.infrastructure.repository.TeamInvitationRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;
import pl.kj.bachelors.teams.infrastructure.service.TokenGenerationService;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

@Service
public class InvitationManagementService implements InvitationManager {
    private final TeamRepository teamRepository;
    private final TeamInvitationRepository invitationRepository;
    private final TeamInvitationConfig config;
    private final TokenGenerationService tokenService;

    @Autowired
    public InvitationManagementService(
            TeamRepository teamRepository,
            TeamInvitationRepository invitationRepository,
            TeamInvitationConfig config,
            TokenGenerationService tokenService) {
        this.teamRepository = teamRepository;
        this.invitationRepository = invitationRepository;
        this.config = config;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional(rollbackFor = ResourceNotFoundException.class)
    public TeamInvitation open(int teamId) throws ResourceNotFoundException, ExecutionException, InterruptedException {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);

        return this.createInvitation(team);
    }

    @Override
    public void close(String code) {
        throw new NotImplementedException();
    }

    private TeamInvitation createInvitation(Team team) throws ExecutionException, InterruptedException {
        String token = this.tokenService.generateToken(
                this.config.getToken().getPrefix(),
                this.config.getToken().getSuffix(),
                this.config.getToken().getContentLength()
        );
        TeamInvitation invitation = new TeamInvitation();
        invitation.setTeam(team);
        invitation.setToken(token);
        invitation.setCode(this.tokenService.generateAlphanumericToken(this.config.getCodeLength()));
        invitation.setExpiresAt(this.createExpiresAt());

        this.invitationRepository.save(invitation);

        return invitation;
    }

    private Calendar createExpiresAt() {
        Calendar expiresAt = Calendar.getInstance();
        expiresAt.add(Calendar.MINUTE, this.config.getValidTimeInMinutes());

        return expiresAt;
    }
}
