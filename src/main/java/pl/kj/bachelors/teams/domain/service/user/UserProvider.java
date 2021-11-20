package pl.kj.bachelors.teams.domain.service.user;

import java.util.Optional;

public interface UserProvider {
    Optional<String> getCurrentUserId();
}
