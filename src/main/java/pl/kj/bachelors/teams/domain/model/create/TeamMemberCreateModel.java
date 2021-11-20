package pl.kj.bachelors.teams.domain.model.create;

import pl.kj.bachelors.teams.domain.model.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeamMemberCreateModel {
    private int teamId;
    private String userId;
    private Set<Role> roles = new HashSet<>();

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> rolesIds) {
        this.roles = rolesIds;
    }
}
