package pl.kj.bachelors.teams.unit.infrastructure.service.crud.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;
import pl.kj.bachelors.teams.domain.exception.ApiError;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.service.user.UserProvider;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.service.crud.create.TeamCreateServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.BDDMockito.given;

public class TeamCreateServiceTests extends BaseTest {
    @Autowired
    private TeamCreateServiceImpl service;
    @Autowired
    private TeamMemberRepository memberRepository;
    @MockBean
    private UserProvider userProvider;
    private final String uid = "uid-1";

    @BeforeEach
    public void setUp() {
        given(this.userProvider.getCurrentUserId()).willReturn(Optional.of(this.uid));
    }

    @Test
    @Transactional
    public void testCreate_Correct() throws Exception {
        TeamCreateModel model = new TeamCreateModel();
        model.setName("Some team");
        model.setName("DEFAULT");
        model.setPictureId(1);

        Team team = this.service.create(model, Team.class);
        assertThat(team.getId()).isPositive();
        assertThat(team.getName()).isEqualTo(model.getName());
        assertThat(team.getPicture()).isNotNull();
        assertThat(team.getPicture().getId()).isEqualTo(model.getPictureId());

        Optional<TeamMember> member = this.memberRepository.findFirstByTeamAndUserId(team, uid);
        assertThat(member).isPresent();
        assertThat(member.get().getRoles()).hasSize(2);
    }

    @Test
    public void testCreate_Correct_WithoutPicture() throws Exception {
        TeamCreateModel model = new TeamCreateModel();
        model.setName("Some team");

        Team team = this.service.create(model, Team.class);
        assertThat(team.getId()).isPositive();
        assertThat(team.getName()).isEqualTo(model.getName());
        assertThat(team.getPicture()).isNull();
    }

    @Test
    public void teamCreate_EmptyName() {
        TeamCreateModel model = new TeamCreateModel();
        model.setName("   ");
        Throwable thrown = catchThrowable(() -> this.service.create(model, Team.class));
        assertThat(thrown).isInstanceOf(AggregatedApiError.class);
        AggregatedApiError ex = (AggregatedApiError) thrown;
        assertThat(ex.getErrors()).hasSize(1);
        ApiError first = ex.getErrors().stream().findFirst().orElseThrow();
        assertThat(first.getCode()).isEqualTo("TM.001");
    }

    @Test
    public void teamCreate_InvalidTheme() {
        TeamCreateModel model = new TeamCreateModel();
        model.setName("Hello");
        model.setTheme("fake-theme");
        Throwable thrown = catchThrowable(() -> this.service.create(model, Team.class));
        assertThat(thrown).isInstanceOf(AggregatedApiError.class);
        AggregatedApiError ex = (AggregatedApiError) thrown;
        assertThat(ex.getErrors()).hasSize(1);
        ApiError first = ex.getErrors().stream().findFirst().orElseThrow();
        assertThat(first.getCode()).isEqualTo("TM.002");
    }
}
