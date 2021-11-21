package pl.kj.bachelors.teams.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.kj.bachelors.teams.application.dto.response.member.TeamMemberResponse;
import pl.kj.bachelors.teams.application.dto.response.page.PageResponse;
import pl.kj.bachelors.teams.domain.annotation.Authentication;
import pl.kj.bachelors.teams.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.service.crud.read.TeamMemberReadService;

import java.util.Map;

@RestController
@RequestMapping("/v1/teams/{teamId}/members")
@Authentication
public class MemberApiController extends BaseApiController {
    private final TeamMemberReadService readService;

    @Autowired
    public MemberApiController(TeamMemberReadService readService) {
        this.readService = readService;
    }

    @GetMapping
    @Transactional
    public ResponseEntity<PageResponse<TeamMemberResponse>> get(
            @PathVariable Integer teamId,
            @RequestParam Map<String, String> params) throws ResourceNotFoundException {
        Page<TeamMember> membersPage = this.readService.readPagedByTeam(teamId, this.createPageable(params));

        return ResponseEntity.ok(this.createPageResponse(membersPage, TeamMemberResponse.class));
    }
}
