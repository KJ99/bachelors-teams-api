package pl.kj.bachelors.teams.domain.model.update;


import pl.kj.bachelors.teams.domain.model.Role;

import java.util.ArrayList;
import java.util.Collection;

public class TeamMemberUpdateModel {
    private Collection<Role> roles = new ArrayList<>();

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
