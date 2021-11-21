package pl.kj.bachelors.teams.unit.infrastructure.service.crud.delete;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;
import pl.kj.bachelors.teams.infrastructure.service.crud.delete.TeamMemberDeleteServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class TeamMemberDeleteServiceTests extends BaseTest {
    @Autowired
    private TeamMemberDeleteServiceImpl service;
    @Autowired
    private TeamMemberRepository repository;
    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void testDeleteByTeamAndUserId() throws Exception {
        Integer teamId = 1;
        Team team = this.teamRepository.findById(teamId).orElseThrow();
        String uid = "uid-11";
        this.service.deleteByTeamAndUserId(teamId, uid);
        assertThat(this.repository.findFirstByTeamAndUserId(team, uid)).isEmpty();
    }

    @Test
    public void testDeleteByTeamAndUserId_AccessDenied() {
        Integer teamId = 1;
        String uid = "uid-1";
        Throwable thrown = catchThrowable(() -> this.service.deleteByTeamAndUserId(teamId, uid));
        assertThat(thrown).isInstanceOf(AccessDeniedException.class);

    }

    @Test
    public void testDeleteByTeamAndUserId_TeamNotFound() {
        Integer teamId = -1;
        String uid = "uid-1";
        Throwable thrown = catchThrowable(() -> this.service.deleteByTeamAndUserId(teamId, uid));
        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    public void testDeleteByTeamAndUserId_MemberNotFound() {
        Integer teamId = 1;
        String uid = "uid-3";
        Throwable thrown = catchThrowable(() -> this.service.deleteByTeamAndUserId(teamId, uid));
        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
    }
}
