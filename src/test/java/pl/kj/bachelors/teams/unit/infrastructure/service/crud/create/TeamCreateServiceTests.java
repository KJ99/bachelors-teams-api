package pl.kj.bachelors.teams.unit.infrastructure.service.crud.create;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;
import pl.kj.bachelors.teams.domain.exception.ApiError;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.infrastructure.service.crud.create.TeamCreateServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class TeamCreateServiceTests extends BaseTest {
    @Autowired
    private TeamCreateServiceImpl service;

    @Test
    public void testCreate_Correct() {
        TeamCreateModel model = new TeamCreateModel();
        model.setName("Some team");
        model.setName("DEFAULT");
        model.setPictureId(1);

        Throwable thrown = catchThrowable(() -> {
            Team team = this.service.create(model, Team.class);
            assertThat(team.getId()).isPositive();
            assertThat(team.getName()).isEqualTo(model.getName());
            assertThat(team.getPicture()).isNotNull();
            assertThat(team.getPicture().getId()).isEqualTo(model.getPictureId());
        });
        assertThat(thrown).isNull();
    }

    @Test
    public void testCreate_Correct_WithoutPicture() {
        TeamCreateModel model = new TeamCreateModel();
        model.setName("Some team");

        Throwable thrown = catchThrowable(() -> {
            Team team = this.service.create(model, Team.class);
            assertThat(team.getId()).isPositive();
            assertThat(team.getName()).isEqualTo(model.getName());
            assertThat(team.getPicture()).isNull();
        });
        assertThat(thrown).isNull();
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
