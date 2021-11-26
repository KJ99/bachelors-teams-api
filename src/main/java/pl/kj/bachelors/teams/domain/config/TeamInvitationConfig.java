package pl.kj.bachelors.teams.domain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "team-invitation")
public class TeamInvitationConfig {
    private int codeLength;
    private int validTimeInMinutes;
    private InvitationTokenConfig token;

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    public int getValidTimeInMinutes() {
        return validTimeInMinutes;
    }

    public void setValidTimeInMinutes(int validTimeInMinutes) {
        this.validTimeInMinutes = validTimeInMinutes;
    }

    public InvitationTokenConfig getToken() {
        return token;
    }

    public void setToken(InvitationTokenConfig token) {
        this.token = token;
    }
}
