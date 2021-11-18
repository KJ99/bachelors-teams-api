package pl.kj.bachelors.teams.infrastructure.service.crud.create;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.service.ModelValidator;
import pl.kj.bachelors.teams.domain.service.crud.create.TeamCreateService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;

@Service
public class TeamCreateServiceImpl
        extends BaseEntityCreateService<Team, Integer, TeamRepository, TeamCreateModel>
        implements TeamCreateService {
    @Autowired
    public TeamCreateServiceImpl(ModelMapper modelMapper, TeamRepository repository, ModelValidator validator) {
        super(modelMapper, repository, validator);
    }
}
