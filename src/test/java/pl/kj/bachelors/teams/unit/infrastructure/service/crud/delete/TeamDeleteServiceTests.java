package pl.kj.bachelors.teams.unit.infrastructure.service.crud.delete;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;
import pl.kj.bachelors.teams.infrastructure.service.crud.delete.TeamDeleteServiceImpl;
import pl.kj.bachelors.teams.unit.BaseUnitTest;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamDeleteServiceTests extends BaseUnitTest {
    @Autowired
    private TeamDeleteServiceImpl service;
    @Autowired
    private TeamRepository repository;

    @Test
    public void testDelete() throws Exception {
        Team team = this.repository.findById(1).orElseThrow();
        int id = team.getId();
        this.service.delete(team);
        assertThat(this.repository.findById(id)).isEmpty();
    }
}
