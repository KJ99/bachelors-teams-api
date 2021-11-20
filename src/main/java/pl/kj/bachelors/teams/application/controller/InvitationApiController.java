package pl.kj.bachelors.teams.application.controller;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kj.bachelors.teams.application.dto.request.InvitationCreateRequest;
import pl.kj.bachelors.teams.application.dto.response.invitation.InvitationCreateResponse;
import pl.kj.bachelors.teams.application.dto.response.invitation.InvitationResponse;
import pl.kj.bachelors.teams.domain.annotation.Authentication;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.TeamInvitation;
import pl.kj.bachelors.teams.domain.service.invitation.InvitationManager;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/v1/invitations")
@Authentication
public class InvitationApiController extends BaseApiController {
    private final InvitationManager manager;

    @Autowired
    public InvitationApiController(InvitationManager manager) {
        this.manager = manager;
    }

    @PostMapping
    private ResponseEntity<InvitationCreateResponse> createInvitation(@RequestBody InvitationCreateRequest model)
            throws ResourceNotFoundException, ExecutionException, InterruptedException {
        TeamInvitation invitation = this.manager.open(model.getTeamId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.map(invitation, InvitationCreateResponse.class));
    }

    @GetMapping("/{code}")
    private ResponseEntity<InvitationResponse> getInvitation(@PathVariable String code) {
        throw new NotImplementedException();
    }

    @DeleteMapping("/{code}")
    private ResponseEntity<?> closeInvitation(@PathVariable String code) throws ResourceNotFoundException {
        this.manager.close(code);

        return ResponseEntity.noContent().build();
    }
}
