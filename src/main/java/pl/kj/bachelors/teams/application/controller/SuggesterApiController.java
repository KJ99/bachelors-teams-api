package pl.kj.bachelors.teams.application.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kj.bachelors.teams.application.dto.response.member.MemberRoleResponse;
import pl.kj.bachelors.teams.application.dto.response.member.TeamRoleResponse;
import pl.kj.bachelors.teams.domain.annotation.Authentication;
import pl.kj.bachelors.teams.domain.model.extension.Role;
import pl.kj.bachelors.teams.domain.model.entity.TeamRole;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRoleRepository;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/v1/suggesters")
@Authentication
@Tag(name = "Suggesters")
public class SuggesterApiController extends BaseApiController {
    private final TeamRoleRepository roleRepository;

    @Autowired
    public SuggesterApiController(TeamRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/roles")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation =  TeamRoleResponse.class))
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Collection<TeamRoleResponse>> getRoles() {
        List<TeamRole> roles = this.roleRepository.findExcluding(List.of(Role.OWNER));
        return ResponseEntity.ok(this.mapCollection(roles, TeamRoleResponse.class));
    }
}
