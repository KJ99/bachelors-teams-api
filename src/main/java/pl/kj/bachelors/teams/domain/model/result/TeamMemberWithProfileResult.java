package pl.kj.bachelors.teams.domain.model.result;

import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.remote.UserProfile;

public class TeamMemberWithProfileResult {
    private TeamMember member;
    private UserProfile profile;

    public TeamMember getMember() {
        return member;
    }

    public void setMember(TeamMember member) {
        this.member = member;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
}
