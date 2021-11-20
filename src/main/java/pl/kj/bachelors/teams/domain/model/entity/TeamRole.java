package pl.kj.bachelors.teams.domain.model.entity;

import pl.kj.bachelors.teams.domain.model.Role;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class TeamRole {
    @Id
    @Enumerated(EnumType.STRING)
    private Role code;
    private String name;

    public Role getCode() {
        return code;
    }

    public void setCode(Role code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
