package pl.kj.bachelors.teams.infrastructure.service.crud.update;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.config.ApiConfig;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;
import pl.kj.bachelors.teams.domain.exception.ApiError;
import pl.kj.bachelors.teams.domain.model.Role;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.update.TeamMemberUpdateModel;
import pl.kj.bachelors.teams.domain.service.crud.update.TeamMemberUpdateService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.service.ValidationService;

import java.util.List;

@Service
public class TeamMemberUpdateServiceImpl
    extends BaseEntityUpdateService<TeamMember, Integer, TeamMemberUpdateModel, TeamMemberRepository>
    implements TeamMemberUpdateService {

    @Autowired
    protected TeamMemberUpdateServiceImpl(TeamMemberRepository repository, ValidationService validationService, ModelMapper modelMapper, ObjectMapper objectMapper) {
        super(repository, validationService, modelMapper, objectMapper);
    }

    @Override
    protected void preUpdate(TeamMember original, TeamMemberUpdateModel updateModel) throws AggregatedApiError {
        boolean hadOwner = original.getRoles().stream().anyMatch(role -> role.getCode().equals(Role.OWNER));
        boolean hasOwner = updateModel.getRoles().stream().anyMatch(role -> role.equals(Role.OWNER));
        if(hadOwner && !hasOwner) {
            var ex = new AggregatedApiError();
            ex.setErrors(List.of(new ApiError("", "TM.011", "roles")));
            throw ex;
        }
    }
}
