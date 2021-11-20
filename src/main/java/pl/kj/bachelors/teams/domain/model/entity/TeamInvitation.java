package pl.kj.bachelors.teams.domain.model.entity;

import pl.kj.bachelors.teams.domain.model.embeddable.Audit;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "team_invitations")
public class TeamInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;
    @Column(unique = true)
    private String token;
    @Column(unique = true)
    private String code;
    @Column(name = "expires_at")
    private Calendar expiresAt;
    @Embedded
    private Audit audit;

    public TeamInvitation() {
        this.audit = new Audit();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Calendar getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Calendar expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
