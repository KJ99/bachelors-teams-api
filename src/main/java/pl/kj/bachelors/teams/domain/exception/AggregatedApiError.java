package pl.kj.bachelors.teams.domain.exception;

import java.util.Collection;

public class AggregatedApiError extends Exception {
    private Collection<ApiError> errors;

    public Collection<ApiError> getErrors() {
        return errors;
    }

    public void setErrors(Collection<ApiError> errors) {
        this.errors = errors;
    }
}
