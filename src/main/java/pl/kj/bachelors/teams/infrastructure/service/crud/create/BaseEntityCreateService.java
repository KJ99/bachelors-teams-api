package pl.kj.bachelors.teams.infrastructure.service.crud.create;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;
import pl.kj.bachelors.teams.domain.service.ModelValidator;

public abstract class BaseEntityCreateService<E, PK, R extends JpaRepository<E, PK>, C> {
    protected final ModelMapper modelMapper;
    protected final R repository;
    protected final ModelValidator validator;

    protected BaseEntityCreateService(ModelMapper modelMapper, R repository, ModelValidator validator) {
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.validator = validator;
    }

    @Transactional
    public E create(C model, Class<E> entityClass) throws AggregatedApiError {
        this.ensureThatModelIsValid(model);
        E entity = this.modelMapper.map(model, entityClass);
        this.postCreate(entity);
        this.repository.save(entity);
        return entity;
    }

    protected void postCreate(E entity) {}

    protected void ensureThatModelIsValid(C model) throws AggregatedApiError {
        var violations = this.validator.validateModel(model);
        if(violations.size() > 0) {
            var ex = new AggregatedApiError();
            ex.setErrors(violations);
            throw ex;
        }
    }
}
