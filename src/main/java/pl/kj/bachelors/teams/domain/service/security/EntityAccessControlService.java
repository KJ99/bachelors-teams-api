package pl.kj.bachelors.teams.domain.service.security;

import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;

public interface EntityAccessControlService <T> {
    void ensureThatUserHasAccess(T subject, Object action) throws AccessDeniedException;
}
