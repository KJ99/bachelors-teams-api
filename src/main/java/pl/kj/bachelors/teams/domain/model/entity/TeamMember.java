package pl.kj.bachelors.teams.domain.model.entity;

import pl.kj.bachelors.teams.domain.model.embeddable.Audit;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "team_members")
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    private String userId;
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;
    @ManyToMany
    @JoinTable(
            name = "team_member_roles",
            joinColumns = @JoinColumn(name = "team_member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<TeamRole> roles;
    @Embedded
    private Audit audit;


    public TeamMember() {
        this.audit = new Audit();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public Set<TeamRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<TeamRole> roles) {
        this.roles = roles;
    }
}
