package pl.kj.bachelors.teams.application.dto.response.member;

import java.util.Collection;

public class TeamMemberResponse {
    private int id;
    private String uid;
    private Collection<TeamRoleResponse> roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Collection<TeamRoleResponse> getRoles() {
        return roles;
    }

    public void setRoles(Collection<TeamRoleResponse> roles) {
        this.roles = roles;
    }
}
