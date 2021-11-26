package pl.kj.bachelors.teams.unit.infrastructure.service.crud.create;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.model.extension.Role;
import pl.kj.bachelors.teams.domain.model.create.TeamMemberCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.infrastructure.service.crud.create.TeamMemberCreateServiceImpl;
import pl.kj.bachelors.teams.unit.BaseUnitTest;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamMemberCreateServiceTests extends BaseUnitTest {
    @Autowired
    private TeamMemberCreateServiceImpl service;

    @Test
    public void testCreate() throws Exception {
        TeamMemberCreateModel model = new TeamMemberCreateModel();
        model.setTeamId(1);
        model.setUserId("user-1");
        model.getRoles().add(Role.TEAM_MEMBER);
        model.getRoles().add(Role.ADMIN);

        TeamMember member = this.service.create(model, TeamMember.class);

        assertThat(member).isNotNull();
        assertThat(member.getId()).isPositive();
        assertThat(member.getTeam()).isNotNull();
        assertThat(member.getTeam().getId()).isEqualTo(1);
        assertThat(member.getUserId()).isEqualTo("user-1");
        assertThat(member.getRoles()).hasSize(2);
    }
}
