package pl.kj.bachelors.teams.application.dto.response;

public class BasicCreatedResponse<T> {
    private T createdId;

    public BasicCreatedResponse() {
        this(null);
    }

    public BasicCreatedResponse(T createdId) {
        this.createdId = createdId;
    }

    public T getCreatedId() {
        return createdId;
    }

    public void setCreatedId(T createdId) {
        this.createdId = createdId;
    }
}
