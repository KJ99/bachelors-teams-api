package pl.kj.bachelors.teams.infrastructure.service.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.config.JwtConfig;
import pl.kj.bachelors.teams.domain.model.remote.UserProfile;
import pl.kj.bachelors.teams.infrastructure.config.IdentityServerConfig;

@Service
public class ProfileRemoteProvider extends BaseRemoteEntityProvider<UserProfile, String>{
    private final IdentityServerConfig config;

    @Autowired
    public ProfileRemoteProvider(JwtConfig jwtConfig, IdentityServerConfig config, ObjectMapper objectMapper) {
        super(jwtConfig, objectMapper);
        this.config = config;
    }

    @Override
    protected String getUrl(String entityId) {
        return String.format("%s/v1/profile/%s", this.config.getHost(), entityId);
    }

    @Override
    protected Class<UserProfile> getModelClass() {
        return UserProfile.class;
    }
}
