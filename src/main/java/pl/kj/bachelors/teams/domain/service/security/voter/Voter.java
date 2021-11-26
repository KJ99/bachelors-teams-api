package pl.kj.bachelors.teams.domain.service.security.voter;

import pl.kj.bachelors.teams.domain.model.extension.AccessVote;

public interface Voter<T> {
    AccessVote vote(T subject, Object action, String userId);
}
