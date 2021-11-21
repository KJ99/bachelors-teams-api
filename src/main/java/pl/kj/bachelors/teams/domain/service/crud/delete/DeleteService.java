package pl.kj.bachelors.teams.domain.service.crud.delete;

public interface DeleteService<E, PK> {
    void delete(E entity) throws Exception;
}
