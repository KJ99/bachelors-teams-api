package pl.kj.bachelors.teams.application.controller;

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
import pl.kj.bachelors.teams.application.dto.request.InvitationCreateRequest;
import pl.kj.bachelors.teams.application.dto.response.error.ValidationErrorResponse;
import pl.kj.bachelors.teams.application.dto.response.invitation.InvitationCreateResponse;
import pl.kj.bachelors.teams.application.dto.response.invitation.InvitationResponse;
import pl.kj.bachelors.teams.application.dto.response.team.TeamResponse;
import pl.kj.bachelors.teams.domain.annotation.Authentication;
import pl.kj.bachelors.teams.domain.exception.AccessDeniedException;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;
import pl.kj.bachelors.teams.domain.service.invitation.InvitationManager;
import pl.kj.bachelors.teams.domain.service.invitation.InvitationProcessor;
import pl.kj.bachelors.teams.domain.service.security.EntityAccessControlService;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/v1/invitations")
@Authentication
@Tag(name = "Invitations")
public class InvitationApiController extends BaseApiController {
    private final InvitationManager manager;
    private final InvitationProcessor processor;

    @Autowired
    public InvitationApiController(
            InvitationManager manager,
            InvitationProcessor processor
    ) {
        this.manager = manager;
        this.processor = processor;
    }

    @PostMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = InvitationCreateResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    private ResponseEntity<InvitationCreateResponse> createInvitation(@RequestBody InvitationCreateRequest model)
            throws ResourceNotFoundException, ExecutionException, InterruptedException, AccessDeniedException {
        TeamInvitation invitation = this.manager.open(model.getTeamId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.map(invitation, InvitationCreateResponse.class));
    }

    @GetMapping("/{code}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = InvitationResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    private ResponseEntity<InvitationResponse> getInvitation(@PathVariable String code)
            throws AccessDeniedException, ResourceNotFoundException {
        TeamInvitation invitation = this.processor.unwrap(code);

        return ResponseEntity.ok(this.map(invitation, InvitationResponse.class));
    }

    @DeleteMapping("/{code}")
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
    private ResponseEntity<?> closeInvitation(@PathVariable String code)
            throws ResourceNotFoundException, AccessDeniedException {
        this.manager.close(code);

        return ResponseEntity.noContent().build();
    }
}
