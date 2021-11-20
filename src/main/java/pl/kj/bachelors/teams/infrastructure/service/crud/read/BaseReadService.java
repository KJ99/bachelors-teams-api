package pl.kj.bachelors.teams.infrastructure.service.crud.read;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public abstract class BaseReadService<E, PK, R extends JpaRepository<E, PK>> {
    protected final R repository;

    protected BaseReadService(R repository) {
        this.repository = repository;
    }

    public Page<E> readPaged(Pageable query) {
        return this.repository.findAll(query);
    }

    public Optional<E> readParticular(PK identity) {
        return this.repository.findById(identity);
    }
}
