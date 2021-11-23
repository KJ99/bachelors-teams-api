package pl.kj.bachelors.teams.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
public class SuggesterApiController extends BaseApiController {
    private final TeamRoleRepository roleRepository;

    @Autowired
    public SuggesterApiController(TeamRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/roles")
    public ResponseEntity<Collection<TeamRoleResponse>> getRoles() {
        List<TeamRole> roles = this.roleRepository.findExcluding(List.of(Role.OWNER));
        return ResponseEntity.ok(this.mapCollection(roles, TeamRoleResponse.class));
    }
}
