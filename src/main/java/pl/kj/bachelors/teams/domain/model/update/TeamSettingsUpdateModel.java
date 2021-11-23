package pl.kj.bachelors.teams.domain.model.update;

import pl.kj.bachelors.teams.domain.constraint.FromEnum;
import pl.kj.bachelors.teams.domain.model.extension.AppTheme;

public class TeamSettingsUpdateModel {
    @FromEnum(enumClass = AppTheme.class, message = "TM.002")
    private String theme;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
