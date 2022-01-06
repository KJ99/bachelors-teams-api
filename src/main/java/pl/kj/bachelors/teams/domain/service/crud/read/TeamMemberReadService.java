package pl.kj.bachelors.teams.domain.service.crud.read;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.result.TeamMemberWithProfileResult;

import java.util.List;
import java.util.Optional;

public interface TeamMemberReadService extends ReadService<TeamMember, Integer, TeamMemberWithProfileResult>{
    Page<TeamMemberWithProfileResult> readPagedByTeam(Integer teamId, Pageable query) throws ResourceNotFoundException;
    List<TeamMemberWithProfileResult> readByTeam(Integer teamId) throws ResourceNotFoundException;
    Optional<TeamMemberWithProfileResult> readParticularByUserId(Integer teamId, String userId) throws ResourceNotFoundException;
}
