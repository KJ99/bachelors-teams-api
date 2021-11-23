package pl.kj.bachelors.teams.unit.infrastructure.service.crud.update;

import com.github.fge.jsonpatch.JsonPatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;
import pl.kj.bachelors.teams.domain.model.extension.AppTheme;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.update.TeamUpdateModel;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;
import pl.kj.bachelors.teams.infrastructure.service.crud.update.TeamUpdateServiceImpl;
import pl.kj.bachelors.teams.model.PatchOperation;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class TeamUpdateServiceTests extends BaseTest {
    @Autowired
    private TeamUpdateServiceImpl service;
    @Autowired
    private TeamRepository repository;

    @Test
    public void testProcessUpdate_Correct() throws IOException {
        Team original = this.repository.findById(1).orElseThrow();
        String newName = "new-name";
        Integer newPictureId = 2;
        String newTheme = "UNICORN";
        PatchOperation[] ops = new PatchOperation[] {
                new PatchOperation("replace", "/name", newName),
                new PatchOperation("replace", "/picture_id", newPictureId),
                new PatchOperation("replace", "/settings/theme", newTheme),
        };
        String patchString = this.serialize(ops);
        JsonPatch patch = JsonPatch.fromJson(this.objectMapper.readTree(patchString));

        Throwable thrown = catchThrowable(() -> this.service.processUpdate(original, patch, TeamUpdateModel.class));

        assertThat(thrown).isNull();
        assertThat(original.getName()).isEqualTo(newName);
        assertThat(original.getSettings().getTheme()).isEqualTo(AppTheme.UNICORN);
        assertThat(original.getPicture()).isNotNull();
        assertThat(original.getPicture().getId()).isEqualTo(newPictureId);
    }

    @Test
    public void testProcessUpdate_ValidationFails() throws IOException {
        Team original = this.repository.findById(1).orElseThrow();
        String newName = " ";
        String newTheme = "foo";
        PatchOperation[] ops = new PatchOperation[] {
                new PatchOperation("replace", "/name", newName),
                new PatchOperation("replace", "/settings/theme", newTheme),
        };
        String patchString = this.serialize(ops);
        JsonPatch patch = JsonPatch.fromJson(this.objectMapper.readTree(patchString));

        Throwable thrown = catchThrowable(() -> this.service.processUpdate(original, patch, TeamUpdateModel.class));

        assertThat(thrown).isInstanceOf(AggregatedApiError.class);
        AggregatedApiError ex = (AggregatedApiError) thrown;
        assertThat(ex.getErrors().size()).isEqualTo(2);
        assertThat(ex.getErrors().stream().anyMatch(item -> item.getCode().equals("TM.001"))).isTrue();
        assertThat(ex.getErrors().stream().anyMatch(item -> item.getCode().equals("TM.002"))).isTrue();
    }
}
