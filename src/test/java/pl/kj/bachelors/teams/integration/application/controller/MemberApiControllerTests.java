package pl.kj.bachelors.teams.integration.application.controller;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import pl.kj.bachelors.teams.integration.BaseIntegrationTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class MemberApiControllerTests extends BaseIntegrationTest {
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
}
