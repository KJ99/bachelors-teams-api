package pl.kj.bachelors.teams.domain.service.crud.read;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReadService<T, PK> {
    Page<T> readPaged(Pageable query);
    Optional<T> readParticular(PK identity);
}
