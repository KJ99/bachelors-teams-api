package pl.kj.bachelors.teams.unit.infrastructure.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.fixture.model.ExampleValidatableModel;
import pl.kj.bachelors.teams.infrastructure.service.ValidationService;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationServiceTests extends BaseTest {
    @Autowired
    private ValidationService service;

    @Test
    public void testValidateModel_NoErrors() {
        var model = ExampleValidatableModel.getValidInstance();

        var result = this.service.validateModel(model);

        assertThat(result).isEmpty();
    }

    @Test
    public void testValidateModel_WithErrors() {
        var model = ExampleValidatableModel.getInvalidInstance();

        var result = this.service.validateModel(model);

        assertThat(result)
                .isNotEmpty()
                .hasSize(3);
        assertThat(result.stream().allMatch(item -> item.getPath() != null)).isTrue();
    }
}
