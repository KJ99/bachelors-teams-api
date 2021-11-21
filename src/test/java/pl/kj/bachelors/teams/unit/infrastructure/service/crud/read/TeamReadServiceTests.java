package pl.kj.bachelors.teams.unit.infrastructure.service.crud.read;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.result.TeamWithParticipationResult;
import pl.kj.bachelors.teams.domain.service.crud.read.TeamReadService;
import pl.kj.bachelors.teams.infrastructure.service.crud.read.TeamReadServiceImpl;
import pl.kj.bachelors.teams.infrastructure.user.UserHandler;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class TeamReadServiceTests extends BaseTest {
    @Autowired
    private TeamReadServiceImpl service;

    MockedStatic<UserHandler> userHandlerMock;

    @BeforeEach
    public void setUp()
    {
        this.userHandlerMock = Mockito.mockStatic(UserHandler.class);

        when(UserHandler.getCurrentUserId()).thenReturn(Optional.of("uid-1"));
    }

    @AfterEach
    public void tearDown() {
        this.userHandlerMock.close();
    }

    @Test
    public void testReadPaged() {
        Pageable pageable = PageRequest.of(0, 2);

        Page<TeamWithParticipationResult> page = this.service.readPaged(pageable);

        assertThat(page.getContent()).isNotEmpty();
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    public void testReadParticular() {
        Optional<TeamWithParticipationResult> team = this.service.readParticular(1);
        assertThat(team).isPresent();
    }

    @Test
    public void testReadParticular_NotFound() {
        Optional<TeamWithParticipationResult> team = this.service.readParticular(-2);
        assertThat(team).isNotPresent();
    }
}
