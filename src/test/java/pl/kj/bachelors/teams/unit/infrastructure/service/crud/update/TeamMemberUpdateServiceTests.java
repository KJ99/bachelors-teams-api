package pl.kj.bachelors.teams.unit.infrastructure.service.crud.update;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;
import pl.kj.bachelors.teams.domain.model.Role;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.update.TeamMemberUpdateModel;
import pl.kj.bachelors.teams.domain.model.update.TeamUpdateModel;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.service.crud.update.TeamMemberUpdateServiceImpl;
import pl.kj.bachelors.teams.model.PatchOperation;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class TeamMemberUpdateServiceTests extends BaseTest {
    @Autowired
    private TeamMemberUpdateServiceImpl service;
    @Autowired
    private TeamMemberRepository memberRepository;

    @Test
    public void testProcessUpdate() throws IOException {
        TeamMember original = this.memberRepository.findById(1).orElseThrow();
        Role toAdd = Role.SCRUM_MASTER;
        PatchOperation[] ops = new PatchOperation[] {
                new PatchOperation("add", "/roles/-", toAdd)
        };
        String patchString = this.serialize(ops);
        JsonPatch patch = JsonPatch.fromJson(this.objectMapper.readTree(patchString));

        Throwable thrown = catchThrowable(() -> this.service.processUpdate(original, patch, TeamMemberUpdateModel.class));
        assertThat(thrown).isNull();
        assertThat(original.getRoles().stream().anyMatch(teamRole -> teamRole.getCode().equals(Role.SCRUM_MASTER))).isTrue();
    }

    @Test
    public void testProcessUpdate_AccessDenied() throws IOException {
        TeamMember original = this.memberRepository.findById(1).orElseThrow();
        PatchOperation[] ops = new PatchOperation[] {
                new PatchOperation("remove", "/roles/0", "")
        };
        String patchString = this.serialize(ops);
        JsonPatch patch = JsonPatch.fromJson(this.objectMapper.readTree(patchString));

        Throwable thrown = catchThrowable(() -> this.service.processUpdate(original, patch, TeamMemberUpdateModel.class));
        assertThat(thrown).isInstanceOf(AggregatedApiError.class);
    }
}
