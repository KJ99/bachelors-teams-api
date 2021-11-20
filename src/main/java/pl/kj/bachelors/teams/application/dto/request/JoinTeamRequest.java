package pl.kj.bachelors.teams.application.dto.request;

public class JoinTeamRequest {
    private String inviteToken;

    public String getInviteToken() {
        return inviteToken;
    }

    public void setInviteToken(String inviteToken) {
        this.inviteToken = inviteToken;
    }
}
