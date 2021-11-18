package pl.kj.bachelors.teams.domain.service.security;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface AccessTokenFetcher {
    Optional<String> getAccessTokenFromRequest(HttpServletRequest request);
}
