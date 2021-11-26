package pl.kj.bachelors.teams.domain.model.update;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public class TeamUpdateModel {
    @NotBlank(message = "TM.001")
    private String name;
    private Integer pictureId;
    @Valid
    private TeamSettingsUpdateModel settings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPictureId() {
        return pictureId;
    }

    public void setPictureId(Integer pictureId) {
        this.pictureId = pictureId;
    }

    public TeamSettingsUpdateModel getSettings() {
        return settings;
    }

    public void setSettings(TeamSettingsUpdateModel settings) {
        this.settings = settings;
    }
}
