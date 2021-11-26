package pl.kj.bachelors.teams.domain.service.user;

import pl.kj.bachelors.teams.domain.model.remote.UserProfile;

import java.util.Optional;

public interface ProfileProvider {
    Optional<UserProfile> get(String uid);
}
