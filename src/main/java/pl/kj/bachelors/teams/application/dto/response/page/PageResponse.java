package pl.kj.bachelors.teams.application.dto.response.page;

import java.util.Collection;
import java.util.List;

public class PageResponse<T> {
    private PageMetadata metadata;
    private Collection<T> data;

    public PageMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(PageMetadata metadata) {
        this.metadata = metadata;
    }

    public Collection<T> getData() {
        return data;
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }
}
