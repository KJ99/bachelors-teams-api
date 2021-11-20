package pl.kj.bachelors.teams.unit.infrastructure.service.crud.read;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.service.crud.read.TeamReadService;
import pl.kj.bachelors.teams.infrastructure.service.crud.read.TeamReadServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamReadServiceTests extends BaseTest {
    @Autowired
    private TeamReadServiceImpl service;

    @Test
    public void testReadPaged() {
        Pageable pageable = PageRequest.of(0, 2);

        Page<Team> page = this.service.readPaged(pageable);

        assertThat(page.getContent()).isNotEmpty();
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    public void testReadParticular() {
        Optional<Team> team = this.service.readParticular(2);
        assertThat(team).isPresent();
    }

    @Test
    public void testReadParticular_NotFound() {
        Optional<Team> team = this.service.readParticular(-2);
        assertThat(team).isNotPresent();
    }
}
