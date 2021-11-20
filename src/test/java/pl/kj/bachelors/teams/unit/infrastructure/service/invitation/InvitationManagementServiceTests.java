package pl.kj.bachelors.teams.unit.infrastructure.service.invitation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;
import pl.kj.bachelors.teams.infrastructure.repository.TeamInvitationRepository;
import pl.kj.bachelors.teams.infrastructure.service.invitation.InvitationManagementService;

import java.util.Calendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

public class InvitationManagementServiceTests extends BaseTest {
    @Autowired
    private InvitationManagementService service;
    @Autowired
    private TeamInvitationRepository invitationRepository;

    @Test
    public void testOpen() {
        final int teamId = 1;

        Throwable thrown = catchThrowable(() -> {
            TeamInvitation invitation = service.open(teamId);

            assertThat(invitation).isNotNull();
            assertThat(invitation.getCode()).isNotEmpty();
            assertThat(invitation.getToken()).isNotEmpty();
            assertThat(invitation.getExpiresAt()).isGreaterThan(Calendar.getInstance());
            assertThat(invitation.getTeam()).isNotNull();
            assertThat(invitation.getTeam().getId()).isEqualTo(teamId);
        });

        assertThat(thrown).isNull();
    }

    @Test
    public void testOpen_TeamNotFound() {
        final int teamId = -1;

        Throwable thrown = catchThrowable(() -> service.open(teamId));

        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void testClose() {
        final String code = "0123456";

        Throwable thrown = catchThrowable(() -> {
            service.close(code);

            Optional<TeamInvitation> invitation = invitationRepository.findFirstByCode(code);

            assertThat(invitation).isNotPresent();
        });

        assertThat(thrown).isNull();
    }

    @Test
    public void testClose_NotFound() {
        final String code = "fake-code";

        Throwable thrown = catchThrowable(() -> service.close(code));

        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
    }

}
