package pl.kj.bachelors.teams.application.dto.response.member;

import pl.kj.bachelors.teams.domain.model.extension.Role;

import java.util.Collection;

public class MemberRoleResponse {
    private String userId;
    private Collection<Role> roles;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
