package pl.kj.bachelors.teams.unit.infrastructure.service.crud.read;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;
import pl.kj.bachelors.teams.infrastructure.service.crud.read.TeamMemberReadServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamMemberReadServiceTest extends BaseTest {
    @Autowired
    private TeamMemberReadServiceImpl service;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void testReadPagedByTeam() throws ResourceNotFoundException {

        Page<TeamMember> page = this.service.readPagedByTeam(1, PageRequest.of(0, 100));
        assertThat(page.getContent()).isNotEmpty();
    }
}
