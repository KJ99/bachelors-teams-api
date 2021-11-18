package pl.kj.bachelors.teams.domain.service.crud.create;

import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;

public interface CreateService<E, PK, R, C> {
    E create(C model, Class<E> entityClass) throws AggregatedApiError;
}
