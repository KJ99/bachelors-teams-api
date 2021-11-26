package pl.kj.bachelors.teams.integration.application.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import pl.kj.bachelors.teams.integration.BaseIntegrationTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class SuggesterApiControllerTests extends BaseIntegrationTest {
    @Test
    public void testGetRoles_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                get("/v1/suggesters/roles")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk());
    }
    @Test
    public void testGetRoles_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/suggesters/roles")
        ).andExpect(status().isUnauthorized());
    }
}
