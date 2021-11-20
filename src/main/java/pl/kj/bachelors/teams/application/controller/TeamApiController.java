package pl.kj.bachelors.teams.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.NotImplementedException;
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
import pl.kj.bachelors.teams.domain.annotation.Authentication;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;
import pl.kj.bachelors.teams.domain.exception.CredentialsNotFoundException;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.update.TeamUpdateModel;
import pl.kj.bachelors.teams.domain.service.crud.create.TeamCreateService;
import pl.kj.bachelors.teams.domain.service.crud.read.TeamReadService;
import pl.kj.bachelors.teams.domain.service.crud.update.TeamUpdateService;
import pl.kj.bachelors.teams.domain.service.invitation.InvitationProcessor;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;

import java.util.Map;

@RestController
@RequestMapping("/v1/teams")
@Tag(name = "Teams")
@Authentication
public class TeamApiController extends BaseApiController {
    private final TeamCreateService createService;
    private final TeamUpdateService updateService;
    private final TeamRepository teamRepository;
    private final TeamReadService teamReadService;
    private final InvitationProcessor invitationProcessor;

    @Autowired
    public TeamApiController(
            TeamCreateService createService,
            TeamRepository teamRepository,
            TeamUpdateService updateService,
            TeamReadService teamReadService,
            InvitationProcessor invitationProcessor
    ) {
        this.createService = createService;
        this.teamRepository = teamRepository;
        this.updateService = updateService;
        this.teamReadService = teamReadService;
        this.invitationProcessor = invitationProcessor;
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
            throws AggregatedApiError, ResourceNotFoundException, JsonPatchException, JsonProcessingException {
        Team team = this.teamRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        this.updateService.processUpdate(team, jsonPatch, TeamUpdateModel.class);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> get(@RequestParam Map<String, String> params) {
        PagingQuery query = this.parseQueryParams(params, PagingQuery.class);
        Pageable pageable = PageRequest.of(query.getPage(), query.getPageSize());
        Page<Team> page = this.teamReadService.readPaged(pageable);
        PageResponse<TeamResponse> response = new PageResponse<>();
        response.setMetadata(this.map(page, PageMetadata.class));
        response.setData(this.mapCollection(page.getContent(), TeamResponse.class));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<TeamResponse> getParticular(@PathVariable int id)
            throws ResourceNotFoundException {
        Team team = this.teamReadService.readParticular(id).orElseThrow(ResourceNotFoundException::new);

        return ResponseEntity.ok(this.map(team, TeamResponse.class));
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody JoinTeamRequest request)
            throws CredentialsNotFoundException, Exception, ResourceNotFoundException {
        String uid = this.getCurrentUserId().orElseThrow(CredentialsNotFoundException::new);
        this.invitationProcessor.joinTeam(uid, request.getInviteToken());

        return ResponseEntity.noContent().build();
    }
}
