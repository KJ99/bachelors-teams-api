package pl.kj.bachelors.teams.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kj.bachelors.teams.application.dto.response.member.MemberRoleResponse;
import pl.kj.bachelors.teams.domain.annotation.Authentication;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.extension.action.TeamCrudAction;
import pl.kj.bachelors.teams.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;

import java.util.Collection;

@RestController
@RequestMapping("/v1/teams/{teamId}/roles")
@Authentication
public class RolesApiController extends BaseApiController {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository memberRepository;
    private final EntityAccessControlService<Team> accessControl;

    @Autowired
    public RolesApiController(
            TeamRepository teamRepository,
            TeamMemberRepository memberRepository,
            EntityAccessControlService<Team> accessControl
    ) {
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
        this.accessControl = accessControl;
    }


    @GetMapping
    public ResponseEntity<Collection<MemberRoleResponse>> get(@PathVariable Integer teamId)
            throws ResourceNotFoundException, AccessDeniedException {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(team, TeamCrudAction.READ);

        return ResponseEntity.ok(this.mapCollection(team.getMembers(), MemberRoleResponse.class));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<MemberRoleResponse> getForUser(
            @PathVariable Integer teamId,
            @PathVariable String userId
    ) throws ResourceNotFoundException, AccessDeniedException {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(team, TeamCrudAction.READ);
        TeamMember member = this.memberRepository
                .findFirstByTeamAndUserId(team, userId)
                .orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(this.map(member, MemberRoleResponse.class));
    }
}
