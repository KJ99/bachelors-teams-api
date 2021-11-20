package pl.kj.bachelors.teams.integration.application.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MvcResult;
import pl.kj.bachelors.teams.application.dto.request.InvitationCreateRequest;
import pl.kj.bachelors.teams.application.dto.response.invitation.InvitationCreateResponse;
import pl.kj.bachelors.teams.integration.BaseIntegrationTest;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
