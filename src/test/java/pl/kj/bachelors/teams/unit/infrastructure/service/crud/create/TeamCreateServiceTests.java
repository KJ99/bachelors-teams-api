package pl.kj.bachelors.teams.unit.infrastructure.service.crud.create;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;
import pl.kj.bachelors.teams.domain.exception.ApiError;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.service.crud.create.TeamCreateServiceImpl;
import pl.kj.bachelors.teams.infrastructure.user.RequestHandler;
import pl.kj.bachelors.teams.unit.BaseUnitTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class TeamCreateServiceTests extends BaseUnitTest {
    @Autowired
    private TeamCreateServiceImpl service;
    @Autowired
    private TeamMemberRepository memberRepository;


    @Override
    protected String getCurrentUserId() {
        return "uid-2";
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

        Optional<TeamMember> member = this.memberRepository.findFirstByTeamAndUserId(team, this.getCurrentUserId());
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
