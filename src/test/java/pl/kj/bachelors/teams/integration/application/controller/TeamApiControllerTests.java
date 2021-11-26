package pl.kj.bachelors.teams.integration.application.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MvcResult;
import pl.kj.bachelors.teams.application.dto.request.JoinTeamRequest;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.integration.BaseIntegrationTest;
import pl.kj.bachelors.teams.model.PatchOperation;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    public void testPatch_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/name", "completely-new-name")
        });

        this.mockMvc.perform(
                patch("/v1/teams/1")
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
                        .header("Authorization", auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testPatch_BadRequest() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/name", "  "),
                new PatchOperation("replace", "/settings/theme", "fake-theme")
        });

        MvcResult result = this.mockMvc.perform(
                patch("/v1/teams/1")
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
                        .header("Authorization", auth)
        ).andExpect(status().isBadRequest()).andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains("name")
                .contains("theme");
    }

    @Test
    public void testPatch_Unauthorized() throws Exception {
        String requestBody = this.serialize(new PatchOperation[] {});

        this.mockMvc.perform(
                patch("/v1/teams/1")
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testPatch_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateExpiredAccessToken("uid-1"));
        String requestBody = this.serialize(new PatchOperation[] {});

        this.mockMvc.perform(
                patch("/v1/teams/1")
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
                        .header("Authorization", auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testPatch_NotFound() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/name", "completely-new-name")
        });

        this.mockMvc.perform(
                patch("/v1/teams/-1")
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
                        .header("Authorization", auth)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testGet_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                get("/v1/teams")
                        .header("Authorization", auth)
        ).andExpect(status().isOk());
    }

    @Test
    public void testGet_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/teams")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGet_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateExpiredAccessToken("uid-1"));

        this.mockMvc.perform(
                get("/v1/teams")
                        .header("Authorization", auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetParticular_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        MvcResult result = this.mockMvc.perform(
                get("/v1/teams/1")
                        .header("Authorization", auth)
        ).andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains("name")
                .contains("theme");
    }

    @Test
    public void testGetParticular_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/teams/1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetParticular_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateExpiredAccessToken("uid-1"));

        this.mockMvc.perform(
                get("/v1/teams/1")
                        .header("Authorization", auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetParticular_NotFound() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));

        this.mockMvc.perform(
                get("/v1/teams/-1")
                        .header("Authorization", auth)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testJoin_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-2"));
        JoinTeamRequest request = new JoinTeamRequest();
        request.setInviteToken("valid-token-1");
        String requestBody = this.serialize(request);

        this.mockMvc.perform(
                post("/v1/teams/join")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("Authorization", auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testJoin_NotFound() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        JoinTeamRequest request = new JoinTeamRequest();
        request.setInviteToken("fake-token");
        String requestBody = this.serialize(request);

        this.mockMvc.perform(
                post("/v1/teams/join")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("Authorization", auth)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testJoin_BadRequest_TokenExpired() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        JoinTeamRequest request = new JoinTeamRequest();
        request.setInviteToken("expired-token-1");
        String requestBody = this.serialize(request);

        MvcResult result = this.mockMvc.perform(
                post("/v1/teams/join")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("Authorization", auth)
        ).andExpect(status().isBadRequest()).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("TM.101");
    }

    @Test
    public void testJoin_BadRequest_AlreadyMember() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        JoinTeamRequest request = new JoinTeamRequest();
        request.setInviteToken("valid-token-1");
        String requestBody = this.serialize(request);

        MvcResult result = this.mockMvc.perform(
                post("/v1/teams/join")
                        .contentType("application/json")
                        .content(requestBody)
                        .header("Authorization", auth)
        ).andExpect(status().isBadRequest()).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("TM.102");
    }

    @Test
    public void testJoin_Unauthorized() throws Exception {
        JoinTeamRequest request = new JoinTeamRequest();
        request.setInviteToken("valid-token-1");
        String requestBody = this.serialize(request);

        this.mockMvc.perform(
                post("/v1/teams/join")
                        .contentType("application/json")
                        .content(requestBody)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testDelete_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                delete("/v1/teams/1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testDelete_Unauthorized() throws Exception {
        this.mockMvc.perform(
                delete("/v1/teams/1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testDelete_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-11"));
        this.mockMvc.perform(
                delete("/v1/teams/1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
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
