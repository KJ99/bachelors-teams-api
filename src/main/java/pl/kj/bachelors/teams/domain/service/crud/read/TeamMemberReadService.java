package pl.kj.bachelors.teams.domain.service.crud.read;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;

import java.util.Optional;

public interface TeamMemberReadService extends ReadService<TeamMember, Integer, TeamMember>{
    Page<TeamMember> readPagedByTeam(Integer teamId, Pageable query) throws ResourceNotFoundException;
    Optional<TeamMember> readParticularByUserId(Integer teamId, String userId) throws ResourceNotFoundException;
}