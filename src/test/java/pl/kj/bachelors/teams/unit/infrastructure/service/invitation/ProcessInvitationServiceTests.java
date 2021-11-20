package pl.kj.bachelors.teams.unit.infrastructure.service.invitation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;
import pl.kj.bachelors.teams.infrastructure.service.invitation.ProcessInvitationService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class ProcessInvitationServiceTests extends BaseTest {
    @Autowired
    private ProcessInvitationService service;

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
}
