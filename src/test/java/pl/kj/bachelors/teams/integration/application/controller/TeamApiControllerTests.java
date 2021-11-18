package pl.kj.bachelors.teams.integration.application.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.integration.BaseIntegrationTest;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TeamApiControllerTests extends BaseIntegrationTest {
    @Test
    public void testPost_Created() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        String requestBody = this.serialize(this.getCorrectCreateModel());

        MvcResult result = this.mockMvc.perform(
                post("/v1/teams")
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
                        .header("Authorization", auth)
        ).andExpect(status().isCreated()).andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains("id")
                .contains("name")
                .contains("theme")
                .contains("settings")
                .contains("picture_url");
    }

    @Test
    public void testPost_BadRequest() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        TeamCreateModel model = this.getCorrectCreateModel();
        model.setName("");
        model.setTheme("fake");
        String requestBody = this.serialize(model);

        MvcResult result = this.mockMvc.perform(
                post("/v1/teams")
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
                        .header("Authorization", auth)
        ).andExpect(status().isBadRequest()).andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains("name")
                .contains("theme");
    }

    @Test
    public void testPost_Unauthorized() throws Exception {
        String requestBody = this.serialize(this.getCorrectCreateModel());

        this.mockMvc.perform(
                post("/v1/teams")
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testPost_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateExpiredAccessToken("uid-1"));
        String requestBody = this.serialize(this.getCorrectCreateModel());

        this.mockMvc.perform(
                post("/v1/teams")
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
                        .header("Authorization", auth)
        ).andExpect(status().isForbidden());
    }

    private TeamCreateModel getCorrectCreateModel() {
        TeamCreateModel model = new TeamCreateModel();
        model.setName("Team-1");
        model.setPictureId(1);
        model.setTheme("DEFAULT");

        return model;
    }
}
