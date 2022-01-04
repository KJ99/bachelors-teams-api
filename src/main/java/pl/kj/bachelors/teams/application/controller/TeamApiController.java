package pl.kj.bachelors.teams.application.controller;

import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kj.bachelors.teams.application.dto.request.JoinTeamRequest;
import pl.kj.bachelors.teams.application.dto.request.PagingQuery;
import pl.kj.bachelors.teams.application.dto.response.error.ValidationErrorResponse;
import pl.kj.bachelors.teams.application.dto.response.page.PageMetadata;
import pl.kj.bachelors.teams.application.dto.response.page.PageResponse;
import pl.kj.bachelors.teams.application.dto.response.team.TeamResponse;
import pl.kj.bachelors.teams.application.example.TeamPageExample;
import pl.kj.bachelors.teams.domain.annotation.Authentication;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.CredentialsNotFoundException;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.extension.action.TeamCrudAction;
import pl.kj.bachelors.teams.domain.model.result.TeamWithParticipationResult;
import pl.kj.bachelors.teams.domain.model.update.TeamUpdateModel;
import pl.kj.bachelors.teams.domain.service.crud.create.TeamCreateService;
import pl.kj.bachelors.teams.domain.service.crud.delete.TeamDeleteService;
import pl.kj.bachelors.teams.domain.service.crud.delete.TeamMemberDeleteService;
import pl.kj.bachelors.teams.domain.service.crud.read.TeamReadService;
import pl.kj.bachelors.teams.domain.service.crud.update.TeamUpdateService;
import pl.kj.bachelors.teams.domain.service.invitation.InvitationProcessor;
import pl.kj.bachelors.teams.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamMemberRepository;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;
import pl.kj.bachelors.teams.infrastructure.user.RequestHandler;

import java.util.Map;

@RestController
@RequestMapping("/v1/teams")
@Tag(name = "Teams")
@Authentication
public class TeamApiController extends BaseApiController{
    private final InvitationProcessor invitationProcessor;
    private final TeamCreateService createService;
    private final TeamUpdateService updateService;
    private final TeamReadService readService;
    private final TeamRepository repository;
    private final TeamMemberRepository memberRepository;
    private final TeamMemberDeleteService memberDeleteService;
    private final EntityAccessControlService<Team> accessControl;
    private final TeamDeleteService deleteService;

    @Autowired
    public TeamApiController(
            InvitationProcessor invitationProcessor,
            TeamCreateService createService,
            TeamUpdateService updateService,
            TeamReadService readService,
            TeamRepository repository,
            TeamMemberRepository memberRepository,
            TeamMemberDeleteService memberDeleteService,
            EntityAccessControlService<Team> accessControl,
            TeamDeleteService deleteService) {
        this.invitationProcessor = invitationProcessor;
        this.createService = createService;
        this.updateService = updateService;
        this.readService = readService;
        this.repository = repository;
        this.memberRepository = memberRepository;
        this.memberDeleteService = memberDeleteService;
        this.accessControl = accessControl;
        this.deleteService = deleteService;
    }


    @PostMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TeamResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorResponse.class))
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<TeamResponse> post(@RequestBody TeamCreateModel model) throws Exception {
        Team resultEntity = this.createService.create(model, Team.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.map(resultEntity, TeamResponse.class));
    }

    @PatchMapping("/{id}")
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
    public ResponseEntity<TeamResponse> patch(@PathVariable int id, @RequestBody JsonPatch jsonPatch)
            throws Exception {
        Team team = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(team, TeamCrudAction.UPDATE);
        this.updateService.processUpdate(team, jsonPatch, TeamUpdateModel.class);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TeamPageExample.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(parameters = {
            @Parameter(name = "params", schema = @Schema(implementation = PagingQuery.class))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> get(@RequestParam Map<String, String> params) {
        PagingQuery query = this.parseQueryParams(params, PagingQuery.class);
        Pageable pageable = PageRequest.of(query.getPage(), query.getPageSize());
        Page<TeamWithParticipationResult> page = this.readService.readPaged(pageable);
        PageResponse<TeamResponse> response = this.createPageResponse(page, TeamResponse.class);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TeamResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<TeamResponse> getParticular(@PathVariable int id)
            throws ResourceNotFoundException, AccessDeniedException {
        TeamWithParticipationResult result = this.readService
                .readParticular(id)
                .orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(result.getTeam(), TeamCrudAction.READ);

        return ResponseEntity.ok(this.map(result, TeamResponse.class));
    }

    @PostMapping("/join")
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
    public ResponseEntity<?> join(@RequestBody JoinTeamRequest request)
            throws CredentialsNotFoundException, Exception {
        String uid = this.getCurrentUserId().orElseThrow(CredentialsNotFoundException::new);
        this.invitationProcessor.joinTeam(uid, request.getInviteToken());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
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
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception {
        Team team = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(team, TeamCrudAction.DELETE);

        this.deleteService.delete(team);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/leave")
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
    public ResponseEntity<?> leaveTeam(@PathVariable Integer id) throws Exception {
        String uid = RequestHandler.getCurrentUserId().orElseThrow(AccessDeniedException::new);
        this.memberDeleteService.deleteByTeamAndUserId(id, uid);

        return ResponseEntity.noContent().build();
    }
}
