package pl.kj.bachelors.teams.domain.service.crud.create;

public interface CreateService<E, C> {
    E create(C model, Class<E> entityClass) throws Exception;
}
