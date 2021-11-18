package pl.kj.bachelors.teams.domain.model.embeddable;

import pl.kj.bachelors.teams.domain.model.AppTheme;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class TeamSettings {
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "settings_theme")
    private AppTheme theme = AppTheme.DEFAULT;

    public AppTheme getTheme() {
        return theme;
    }

    public void setTheme(AppTheme theme) {
        this.theme = theme;
    }
}
