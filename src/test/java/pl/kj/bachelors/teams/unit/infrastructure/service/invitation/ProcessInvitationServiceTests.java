package pl.kj.bachelors.teams.unit.infrastructure.service.invitation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.Role;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;
import pl.kj.bachelors.teams.infrastructure.service.invitation.ProcessInvitationService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class ProcessInvitationServiceTests extends BaseTest {
    @Autowired
    private ProcessInvitationService service;

    @Autowired
    private TeamMemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void testUnwrap() {
        final String code = "0123456";
        Throwable thrown = catchThrowable(() -> {
            TeamInvitation invitation = this.service.unwrap(code);

            assertThat(invitation.getCode()).isEqualTo(code);
        });
        assertThat(thrown).isNull();
    }

    @Test
    public void testUnwrap_NotFound() {
        final String code = "fake-code";
        Throwable thrown = catchThrowable(() -> this.service.unwrap(code));
        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void testUnwrap_Expired() {
        final String code = "07821";
        Throwable thrown = catchThrowable(() -> this.service.unwrap(code));
        assertThat(thrown).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @Transactional
    public void testJoinTeam() throws Exception, ResourceNotFoundException {
        final Team team = this.teamRepository.findById(1).orElseThrow();
        final String uid = "uid-1";
        final String token = "valid-token-1";

        this.service.joinTeam(uid, token);
        Optional<TeamMember> member = this.memberRepository.findFirstByTeamAndUserId(team, uid);
        assertThat(member).isPresent();
        assertThat(member.get().getRoles().stream().anyMatch(role -> role.getCode().equals(Role.TEAM_MEMBER))).isTrue();
    }

    @Test
    public void testJoinTeam_NotFound() {
        final String uid = "uid-1";
        final String token = "";

        Throwable thrown = catchThrowable(() -> this.service.joinTeam(uid, token));

        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void testJoinTeam_AccessDenied() {
        final String uid = "uid-1";
        final String token = "expired-token-1";

        Throwable thrown = catchThrowable(() -> this.service.joinTeam(uid, token));

        assertThat(thrown).isInstanceOf(AccessDeniedException.class);
    }
}
