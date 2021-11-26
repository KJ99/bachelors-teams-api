package pl.kj.bachelors.teams.unit.infrastructure.service.crud.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.remote.UserProfile;
import pl.kj.bachelors.teams.domain.model.result.TeamMemberWithProfileResult;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;
import pl.kj.bachelors.teams.infrastructure.service.crud.read.TeamMemberReadServiceImpl;
import pl.kj.bachelors.teams.infrastructure.service.remote.ProfileRemoteProvider;
import pl.kj.bachelors.teams.unit.BaseUnitTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class TeamMemberReadServiceTest extends BaseUnitTest {
    @Autowired
    private TeamMemberReadServiceImpl service;

    @Autowired
    private TeamRepository teamRepository;

    @MockBean
    private ProfileRemoteProvider profileRemoteProvider;

    @BeforeEach
    public void setUp() {
        UserProfile profile = new UserProfile();
        given(profileRemoteProvider.get(anyString())).willReturn(Optional.of(profile));
    }

    @Test
    public void testReadPagedByTeam() throws ResourceNotFoundException {
        Page<TeamMemberWithProfileResult> page = this.service.readPagedByTeam(1, PageRequest.of(0, 100));
        assertThat(page.getContent()).isNotEmpty();
    }

    @Test
    public void testReadParticular() {
        Optional<TeamMemberWithProfileResult> member = this.service.readParticular(1);
        assertThat(member).isPresent();
    }

    @Test
    public void testReadParticularByUserId() throws ResourceNotFoundException {
        Optional<TeamMemberWithProfileResult> member = this.service.readParticularByUserId(1, "uid-1");
        assertThat(member).isPresent();
    }
}
