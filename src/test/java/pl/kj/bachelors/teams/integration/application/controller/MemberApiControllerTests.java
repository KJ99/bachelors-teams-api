package pl.kj.bachelors.teams.integration.application.controller;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.kj.bachelors.teams.domain.model.extension.Role;
import pl.kj.bachelors.teams.domain.model.remote.UserProfile;
import pl.kj.bachelors.teams.infrastructure.service.remote.ProfileRemoteProvider;
import pl.kj.bachelors.teams.integration.BaseIntegrationTest;
import pl.kj.bachelors.teams.model.PatchOperation;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class MemberApiControllerTests extends BaseIntegrationTest {
    @MockBean
    private ProfileRemoteProvider profileRemoteProvider;

    @BeforeEach
    public void setUp() {
        UserProfile profile = new UserProfile();
        profile.setId("uid-000");
        profile.setFirstName("John");
        profile.setLastName("Doe");

        given(profileRemoteProvider.get(anyString())).willReturn(Optional.of(profile));
    }

    @Test
    public void testGet_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                get("/v1/teams/1/members")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk());
    }

    @Test
    public void testGet_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/teams/1/members")
        ).andExpect(status().isUnauthorized());

    }

    @Test
    public void testGetParticular_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                get("/v1/teams/1/members/uid-1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk());
    }

    @Test
    public void testGetParticular_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/teams/1/members/uid-1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testPatch_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("add", "/roles/-", Role.SCRUM_MASTER)
        });
        this.mockMvc.perform(
                patch("/v1/teams/1/members/uid-1")
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testPatch_BadRequest() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("remove", "/roles/0", "")
        });
        this.mockMvc.perform(
                patch("/v1/teams/1/members/uid-1")
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testPatch_Unauthorized() throws Exception {
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("add", "/roles/-", Role.SCRUM_MASTER)
        });
        this.mockMvc.perform(
                patch("/v1/teams/1/members/uid-1")
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testDelete_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                delete("/v1/teams/1/members/uid-11")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testDelete_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                delete("/v1/teams/1/members/uid-1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testDelete_Unauthorized() throws Exception {
        this.mockMvc.perform(
                delete("/v1/teams/1/members/uid-11")
        ).andExpect(status().isUnauthorized());
    }
}
