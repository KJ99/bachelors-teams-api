package pl.kj.bachelors.teams.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kj.bachelors.teams.application.dto.response.member.MemberRoleResponse;
import pl.kj.bachelors.teams.domain.annotation.Authentication;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;

import java.util.Collection;

@RestController
@RequestMapping("/v1/teams/{teamId}/roles")
@Authentication
public class RolesApiController extends BaseApiController {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository memberRepository;

    @Autowired
    public RolesApiController(TeamRepository teamRepository, TeamMemberRepository memberRepository) {
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
    }


    @GetMapping
    public ResponseEntity<Collection<MemberRoleResponse>> get(@PathVariable Integer teamId) throws ResourceNotFoundException {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);

        return ResponseEntity.ok(this.mapCollection(team.getMembers(), MemberRoleResponse.class));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<MemberRoleResponse> getForUser(
            @PathVariable Integer teamId,
            @PathVariable String userId
    ) throws ResourceNotFoundException {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);
        TeamMember member = this.memberRepository
                .findFirstByTeamAndUserId(team, userId)
                .orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(this.map(member, MemberRoleResponse.class));
    }
}