package pl.kj.bachelors.teams.infrastructure.service.remote;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.kj.bachelors.teams.domain.config.JwtConfig;
import pl.kj.bachelors.teams.domain.service.cache.CacheManager;
import pl.kj.bachelors.teams.infrastructure.user.RequestHandler;

import java.util.Optional;

public abstract class BaseRemoteEntityProvider<T, ID> {
    private final JwtConfig jwtConfig;

    protected BaseRemoteEntityProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    protected abstract String getUrl(ID entityId);
    protected abstract Class<T> getModelClass();

    public Optional<T> get(ID entityId) {
        var restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>("parameters", this.getHeaders());
        ResponseEntity<T> response = restTemplate.exchange(
                this.getUrl(entityId),
                HttpMethod.GET, entity,
                this.getModelClass()
        );
        T result;
        if(response.getStatusCodeValue() >= 200 && response.getStatusCodeValue() <= 299) {
            result = response.getBody();
        } else {
            result = null;
        }

        return Optional.ofNullable(result);
    }

    protected HttpHeaders getHeaders() {
        var headers = new HttpHeaders();
        if(this.shouldPassAuthorizationHeader()) {
            headers.add(HttpHeaders.AUTHORIZATION, this.createAuthorization());
        }

        if(this.shouldPassCacheControl()) {
            headers.add(HttpHeaders.CACHE_CONTROL, RequestHandler.getHeaderValue(HttpHeaders.CACHE_CONTROL).orElse(""));
        }

        return headers;
    }

    protected String createAuthorization() {
        String[] headerValueParts =  RequestHandler
                .getHeaderValue(HttpHeaders.AUTHORIZATION)
                .orElse("")
                .split(" ");

        String token = "";
        if(headerValueParts.length > 1 && headerValueParts[0].equals(this.jwtConfig.getType())) {
            token = headerValueParts[1];
        }

        return String.format(
                "%s %s",
                this.jwtConfig.getType(),
                token
        );
    }

    protected boolean shouldPassAuthorizationHeader() {
        return true;
    }

    protected boolean shouldPassCacheControl() {
        return false;
    }
}
