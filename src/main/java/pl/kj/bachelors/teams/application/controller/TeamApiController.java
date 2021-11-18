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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kj.bachelors.teams.application.dto.response.error.ValidationErrorResponse;
import pl.kj.bachelors.teams.application.dto.response.team.TeamResponse;
import pl.kj.bachelors.teams.domain.annotation.Authentication;
import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.update.TeamUpdateModel;
import pl.kj.bachelors.teams.domain.service.crud.create.TeamCreateService;
import pl.kj.bachelors.teams.domain.service.crud.update.TeamUpdateService;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRepository;

@RestController
@RequestMapping("/v1/teams")
@Tag(name = "Teams")
@Authentication
public class TeamApiController extends BaseApiController {
    private final TeamCreateService createService;
    private final TeamUpdateService updateService;
    private final TeamRepository teamRepository;

    @Autowired
    public TeamApiController(
            TeamCreateService createService,
            TeamRepository teamRepository,
            TeamUpdateService updateService
    ) {
        this.createService = createService;
        this.teamRepository = teamRepository;
        this.updateService = updateService;
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
    public ResponseEntity<TeamResponse> post(@RequestBody TeamCreateModel model) throws AggregatedApiError {
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
}
