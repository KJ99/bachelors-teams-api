package pl.kj.bachelors.teams.integration.application.controller;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpHead;
import org.junit.jupiter.api.Test;
import pl.kj.bachelors.teams.integration.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RolesApiControllerTests extends BaseIntegrationTest {
    @Test
    public void testGet_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                get("/v1/teams/1/roles")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk());
    }

    @Test
    public void testGet_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/teams/1/roles")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetParticular_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                get("/v1/teams/1/roles/uid-1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk());
    }

    @Test
    public void testGetParticular_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/teams/1/roles/uid-1")
        ).andExpect(status().isUnauthorized());
    }
}
