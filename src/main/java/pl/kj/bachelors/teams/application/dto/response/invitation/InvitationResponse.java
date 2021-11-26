package pl.kj.bachelors.teams.application.dto.response.invitation;

import pl.kj.bachelors.teams.application.dto.response.team.TeamSimpleResponse;

public class InvitationResponse {
    private String token;
    private TeamSimpleResponse team;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TeamSimpleResponse getTeam() {
        return team;
    }

    public void setTeam(TeamSimpleResponse team) {
        this.team = team;
    }
}
