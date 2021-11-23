package pl.kj.bachelors.teams.application.dto.response.member;

import java.util.Collection;

public class TeamMemberResponse {
    private int id;
    private String userId;
    private String firstName;
    private String lastName;
    private String pictureUrl;
    private Collection<TeamRoleResponse> roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<TeamRoleResponse> getRoles() {
        return roles;
    }

    public void setRoles(Collection<TeamRoleResponse> roles) {
        this.roles = roles;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
