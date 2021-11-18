package pl.kj.bachelors.teams.application.dto.response.team;

import pl.kj.bachelors.teams.domain.model.embeddable.TeamSettings;

public class TeamResponse {
    private int id;
    private String name;
    private String pictureUrl;
    private TeamSettingsResponse settings;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamSettingsResponse getSettings() {
        return settings;
    }

    public void setSettings(TeamSettingsResponse settings) {
        this.settings = settings;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
