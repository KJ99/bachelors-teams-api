package pl.kj.bachelors.teams.domain.service.crud.delete;

import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;

public interface TeamMemberDeleteService extends DeleteService<TeamMember, Integer> {
    void deleteByTeamAndUserId(Integer teamId, String userId) throws Exception;
}
