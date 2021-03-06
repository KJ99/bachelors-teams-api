package pl.kj.bachelors.teams.integration.application.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MvcResult;
import pl.kj.bachelors.teams.application.dto.request.InvitationCreateRequest;
import pl.kj.bachelors.teams.application.dto.response.invitation.InvitationCreateResponse;
import pl.kj.bachelors.teams.application.dto.response.invitation.InvitationResponse;
import pl.kj.bachelors.teams.integration.BaseIntegrationTest;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InvitationApiControllerTests extends BaseIntegrationTest {
    @Test
    public void testCreateInvitation_Created() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        InvitationCreateRequest request = new InvitationCreateRequest();
        request.setTeamId(1);
        String requestBody = this.serialize(request);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/v1/invitations")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
        ).andExpect(status().isCreated()).andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        InvitationCreateResponse result = this.deserialize(responseContent, InvitationCreateResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isNotEmpty();
    }

    @Test
    public void testCreateInvitation_NotFound() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        InvitationCreateRequest request = new InvitationCreateRequest();
        request.setTeamId(-1);
        String requestBody = this.serialize(request);

        this.mockMvc.perform(
                post("/v1/invitations")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testCreateInvitation_Unauthorized() throws Exception {
        InvitationCreateRequest request = new InvitationCreateRequest();
        request.setTeamId(1);
        String requestBody = this.serialize(request);

        this.mockMvc.perform(
                post("/v1/invitations")
                        .contentType("application/json")
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testCloseInvitation_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));

        this.mockMvc.perform(
                delete("/v1/invitations/0123456")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testCloseInvitation_NotFound() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));

        this.mockMvc.perform(
                delete("/v1/invitations/-1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testCloseInvitation_Unauthorized() throws Exception {
        this.mockMvc.perform(
                delete("/v1/invitations/0123456")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetInvitation_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));

        MvcResult mvcResult = this.mockMvc.perform(
                get("/v1/invitations/0123456")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk()).andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        InvitationResponse result = this.deserialize(responseContent, InvitationResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isNotEmpty();
        assertThat(result.getTeam()).isNotNull();
        assertThat(result.getTeam().getId()).isPositive();
        assertThat(result.getTeam().getName()).isNotEmpty();
    }

    @Test
    public void testGetInvitation_NotFound() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));

        this.mockMvc.perform(
                get("/v1/invitations/-1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testGetInvitation_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));

        this.mockMvc.perform(
                get("/v1/invitations/07821")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetInvitation_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/invitations/0123456")
        ).andExpect(status().isUnauthorized());
    }
}
