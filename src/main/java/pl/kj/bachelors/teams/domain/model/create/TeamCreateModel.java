package pl.kj.bachelors.teams.domain.model.create;

import pl.kj.bachelors.teams.domain.constraint.FromEnum;
import pl.kj.bachelors.teams.domain.model.extension.AppTheme;

import javax.validation.constraints.NotBlank;

public class TeamCreateModel {
    @NotBlank(message = "TM.001")
    private String name;
    private Integer pictureId;
    @FromEnum(enumClass = AppTheme.class, message = "TM.002")
    private String theme;

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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
