package pl.kj.bachelors.teams.application.controller;

import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.kj.bachelors.teams.application.dto.request.PagingQuery;
import pl.kj.bachelors.teams.application.dto.response.error.ValidationErrorResponse;
import pl.kj.bachelors.teams.application.dto.response.invitation.InvitationResponse;
import pl.kj.bachelors.teams.application.dto.response.member.TeamMemberResponse;
import pl.kj.bachelors.teams.application.dto.response.page.PageResponse;
import pl.kj.bachelors.teams.application.example.TeamMemberPageExample;
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
@Tag(name = "Members")
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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TeamMemberPageExample.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(parameters = {
            @Parameter(name = "params", schema = @Schema(implementation = PagingQuery.class))
    })
    @SecurityRequirement(name = "JWT")
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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TeamMemberResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorResponse.class))
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> delete(@PathVariable Integer teamId, @PathVariable String userId) throws Exception {
        Team team = this.teamRepository.findById(teamId).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(team, TeamMemberAction.UPDATE);
        this.deleteService.deleteByTeamAndUserId(teamId, userId);
        return ResponseEntity.noContent().build();
    }
}
