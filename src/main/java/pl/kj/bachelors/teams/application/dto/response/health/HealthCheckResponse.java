package pl.kj.bachelors.teams.application.dto.response.health;

import java.util.ArrayList;
import java.util.Collection;

public class HealthCheckResponse {
    private Collection<SingleCheckResponse> results;

    public HealthCheckResponse() {
        this.results = new ArrayList<>();
    }

    public Collection<SingleCheckResponse> getResults() {
        return results;
    }

    public void setResults(Collection<SingleCheckResponse> results) {
        this.results = results;
    }
}
