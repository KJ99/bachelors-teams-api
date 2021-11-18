package pl.kj.bachelors.teams.domain.model.entity;

import org.hibernate.annotations.Fetch;
import pl.kj.bachelors.teams.domain.model.embeddable.Audit;
import pl.kj.bachelors.teams.domain.model.embeddable.TeamSettings;

import javax.persistence.*;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picture_id", referencedColumnName = "id")
    private UploadedFile picture;

    @Embedded
    private Audit audit;

    @Embedded
    private TeamSettings settings;

    public Team() {
        this.audit = new Audit();
        this.settings = new TeamSettings();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UploadedFile getPicture() {
        return picture;
    }

    public void setPicture(UploadedFile picture) {
        this.picture = picture;
    }

    public TeamSettings getSettings() {
        return settings;
    }

    public void setSettings(TeamSettings settings) {
        this.settings = settings;
    }
}
