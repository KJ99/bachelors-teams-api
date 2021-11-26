package pl.kj.bachelors.teams.infrastructure.service.invitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;
import pl.kj.bachelors.teams.domain.exception.ApiError;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.create.TeamMemberCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.extension.action.TeamInvitationActions;
import pl.kj.bachelors.teams.domain.service.crud.create.TeamMemberCreateService;
import pl.kj.bachelors.teams.domain.service.invitation.InvitationProcessor;
import pl.kj.bachelors.teams.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamInvitationRepository;

import java.util.Calendar;
import java.util.List;

@Service
public class ProcessInvitationService implements InvitationProcessor {
    private final TeamInvitationRepository invitationRepository;
    private final TeamMemberCreateService memberCreateService;

    @Autowired
    public ProcessInvitationService(
            TeamInvitationRepository invitationRepository,
            TeamMemberCreateService memberCreateService,
            EntityAccessControlService<TeamInvitation> accessControl) {
        this.invitationRepository = invitationRepository;
        this.memberCreateService = memberCreateService;
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
    @Transactional(rollbackFor = {ResourceNotFoundException.class, AggregatedApiError.class})
    public void joinTeam(String uid, String invitationToken) throws Exception {
        TeamInvitation invitation = this.invitationRepository
                .findFirstByToken(invitationToken)
                .orElseThrow(ResourceNotFoundException::new);
        if(invitation.getExpiresAt().getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
            AggregatedApiError ex = new AggregatedApiError();
            ex.setErrors(List.of(new ApiError("", "TM.101", null)));
            throw ex;
        }
        if(invitation.getTeam().getMembers().stream().anyMatch(member -> member.getUserId().equals(uid))) {
            AggregatedApiError ex = new AggregatedApiError();
            ex.setErrors(List.of(new ApiError("", "TM.102", null)));
            throw ex;
        }

        TeamMemberCreateModel model = new TeamMemberCreateModel();
        model.setTeamId(invitation.getTeam().getId());
        model.setUserId(uid);

        this.memberCreateService.create(model, TeamMember.class);
    }
}
