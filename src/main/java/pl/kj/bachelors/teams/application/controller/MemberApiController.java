package pl.kj.bachelors.teams.application.controller;

import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.kj.bachelors.teams.application.dto.response.member.TeamMemberResponse;
import pl.kj.bachelors.teams.application.dto.response.page.PageResponse;
import pl.kj.bachelors.teams.domain.annotation.Authentication;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.extension.action.TeamMemberAction;
import pl.kj.bachelors.teams.domain.model.result.TeamMemberWithProfileResult;
import pl.kj.bachelors.teams.domain.model.update.TeamMemberUpdateModel;
import pl.kj.bachelors.teams.domain.service.crud.delete.TeamMemberDeleteService;
import pl.kj.bachelors.teams.domain.service.crud.read.TeamMemberReadService;
import pl.kj.bachelors.teams.domain.service.crud.update.TeamMemberUpdateService;
import pl.kj.bachelors.teams.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;

import java.util.Map;

@RestController
@RequestMapping("/v1/teams/{teamId}/members")
@Authentication
public class MemberApiController extends BaseApiController {
    private final TeamMemberReadService readService;
    private final TeamMemberRepository repository;
    private final TeamRepository teamRepository;
    private final TeamMemberUpdateService updateService;
    private final TeamMemberDeleteService deleteService;
    private final EntityAccessControlService<Team> accessControl;

    @Autowired
    public MemberApiController(
            TeamMemberReadService readService,
            TeamMemberRepository repository,
            TeamRepository teamRepository,
            TeamMemberUpdateService updateService,
            TeamMemberDeleteService deleteService, EntityAccessControlService<Team> accessControl) {
        this.readService = readService;
        this.repository = repository;
        this.teamRepository = teamRepository;
        this.updateService = updateService;
        this.deleteService = deleteService;
        this.accessControl = accessControl;
    }

    @GetMapping
    @Transactional
    public ResponseEntity<PageResponse<TeamMemberResponse>> get(
            @PathVariable Integer teamId,
            @RequestParam Map<String, String> params) throws ResourceNotFoundException, AccessDeniedException {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(team, TeamMemberAction.READ);
        Page<TeamMemberWithProfileResult> membersPage = this.readService
                .readPagedByTeam(teamId, this.createPageable(params));

        return ResponseEntity.ok(this.createPageResponse(membersPage, TeamMemberResponse.class));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<TeamMemberResponse> getParticular(@PathVariable Integer teamId, @PathVariable String userId)
            throws ResourceNotFoundException, AccessDeniedException {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(team, TeamMemberAction.READ);
        TeamMemberWithProfileResult member = this.readService
                .readParticularByUserId(teamId, userId)
                .orElseThrow(ResourceNotFoundException::new);

        return ResponseEntity.ok(this.map(member, TeamMemberResponse.class));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> patch(
            @PathVariable Integer teamId,
            @PathVariable String userId,
            @RequestBody JsonPatch jsonPatch
            )
            throws Exception {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(team, TeamMemberAction.UPDATE);
        TeamMember member = this.repository
                .findFirstByTeamAndUserId(team, userId)
                .orElseThrow(ResourceNotFoundException::new);

        this.updateService.processUpdate(member, jsonPatch, TeamMemberUpdateModel.class);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable Integer teamId, @PathVariable String userId) throws Exception {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(team, TeamMemberAction.UPDATE);
        this.deleteService.deleteByTeamAndUserId(teamId, userId);
        return ResponseEntity.noContent().build();
    }
}
