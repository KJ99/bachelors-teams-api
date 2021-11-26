package pl.kj.bachelors.teams.domain.service.crud.update;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;

public interface UpdateService<T, U> {
    void processUpdate(T original, JsonPatch patch, Class<U> updateModelClass) throws Exception;
}
