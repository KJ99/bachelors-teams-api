package pl.kj.bachelors.teams.application.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kj.bachelors.teams.application.dto.response.error.ValidationErrorResponse;
import pl.kj.bachelors.teams.application.dto.response.team.TeamResponse;
import pl.kj.bachelors.teams.domain.annotation.Authentication;
import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.service.crud.create.TeamCreateService;

@RestController
@RequestMapping("/v1/teams")
@Tag(name = "Teams")
@Authentication
public class TeamApiController extends BaseApiController {
    private final TeamCreateService createService;

    @Autowired
    public TeamApiController(TeamCreateService createService) {
        this.createService = createService;
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
}
