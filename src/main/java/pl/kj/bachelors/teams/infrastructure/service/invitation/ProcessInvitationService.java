package pl.kj.bachelors.teams.infrastructure.service.invitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.service.invitation.InvitationProcessor;
import pl.kj.bachelors.teams.infrastructure.repository.TeamInvitationRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;

import java.util.Calendar;

@Service
public class ProcessInvitationService implements InvitationProcessor {
    private final TeamInvitationRepository invitationRepository;
    private final TeamMemberRepository memberRepository;

    @Autowired
    public ProcessInvitationService(TeamInvitationRepository invitationRepository, TeamMemberRepository memberRepository) {
        this.invitationRepository = invitationRepository;
        this.memberRepository = memberRepository;
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
    @Transactional(rollbackFor = {ResourceNotFoundException.class, AccessDeniedException.class})
    public void joinTeam(String uid, String invitationToken) throws ResourceNotFoundException, AccessDeniedException {
        TeamInvitation invitation = this.invitationRepository
                .findFirstByToken(invitationToken)
                .orElseThrow(ResourceNotFoundException::new);
        if(invitation.getExpiresAt().getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
            throw new AccessDeniedException("Invitation expired!");
        }

        TeamMember member = new TeamMember();
        member.setTeam(invitation.getTeam());
        member.setUserId(uid);

        this.memberRepository.save(member);
    }
}
