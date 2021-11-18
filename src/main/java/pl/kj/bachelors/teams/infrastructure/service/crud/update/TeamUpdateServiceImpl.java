package pl.kj.bachelors.teams.infrastructure.service.crud.update;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.update.TeamUpdateModel;
import pl.kj.bachelors.teams.domain.service.crud.update.TeamUpdateService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;
import pl.kj.bachelors.teams.infrastructure.service.ValidationService;

@Service
public class TeamUpdateServiceImpl
        extends BaseEntityUpdateService<Team, Integer, TeamUpdateModel, TeamRepository>
        implements TeamUpdateService {

    @Autowired
    protected TeamUpdateServiceImpl(
            TeamRepository repository,
            ValidationService validationService,
            ModelMapper modelMapper,
            ObjectMapper objectMapper
    ) {
        super(repository, validationService, modelMapper, objectMapper);
    }
}
