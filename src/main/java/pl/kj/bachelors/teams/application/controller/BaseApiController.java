package pl.kj.bachelors.teams.application.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.kj.bachelors.teams.application.dto.response.error.GenericErrorResponse;
import pl.kj.bachelors.teams.application.dto.response.error.ValidationErrorResponse;
import pl.kj.bachelors.teams.domain.config.ApiConfig;
import pl.kj.bachelors.teams.domain.exception.*;
import pl.kj.bachelors.teams.domain.service.ModelValidator;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.NoSuchFileException;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

abstract class BaseApiController {
    @Autowired
    protected ModelMapper mapper;
    @Autowired
    protected ModelValidator validator;
    @Autowired
    protected ApiConfig apiConfig;
    @Autowired
    protected HttpServletRequest currentRequest;
    @Autowired(required = false)
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected  <T> T map(Object source, Class<T> destinationType) {
        return this.mapper.map(source, destinationType);
    }

    protected  <T, K> Collection<T> mapCollection(Collection<K> source, Class<T> destinationType) {
        return source
                .stream()
                .map(item -> this.map(item, destinationType))
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = { ResourceNotFoundException.class, NoSuchFileException.class, NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ValidationErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex
    ) {
        final var bodyBuilder = ResponseEntity.status(HttpStatus.CONFLICT);
        final ApiError violation = this.processDataIntegrityViolation(ex);
        return violation != null
                ? bodyBuilder.body(this.map(violation, ValidationErrorResponse.class))
                : bodyBuilder.build();
    }



    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CredentialsNotFoundException.class)
    protected ResponseEntity<Object> handleNotAuthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AggregatedApiError.class)
    protected ResponseEntity<Collection<ValidationErrorResponse>> handleAggregatedApiError(AggregatedApiError ex) {
        return ResponseEntity.badRequest().body(this.mapCollection(ex.getErrors(), ValidationErrorResponse.class));
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiError.class)
    protected ResponseEntity<ValidationErrorResponse> handleRequestViolation(ApiError ex) {
        return ResponseEntity.badRequest().body(this.map(ex, ValidationErrorResponse.class));
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<?> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = { MalformedJwtException.class, JwtInvalidException.class })
    protected ResponseEntity<?> handleJwtReject() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = { ExpiredJwtException.class })
    protected ResponseEntity<GenericErrorResponse<?>> handleJwtExpired() {
        var response = new GenericErrorResponse<>();
        response.setDetailCode("ID.101");
        response.setDetailMessage(this.apiConfig.getErrors().get("ID.101"));

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    protected <T> void ensureThatModelIsValid(T model) throws AggregatedApiError {
        var violations = this.validator.validateModel(model);
        if(violations.size() > 0) {
            var ex = new AggregatedApiError();
            ex.setErrors(violations);
            throw ex;
        }
    }

    private ApiError processDataIntegrityViolation(DataIntegrityViolationException source) {
        String specificMessage = source.getMostSpecificCause().getMessage();

        String code = null;
        String path = null;
        if (this.isMessageContaining(specificMessage, "UN_EMAIL")) {
            code = "ID.012";
            path = "email";
        } else if (this.isMessageContaining(specificMessage, "UN_USERNAME")) {
            code = "ID.011";
            path = "username";
        }

        String message = this.apiConfig.getErrors().get(code);

        return path != null ? new ApiError(message, code, path) : null;
    }

    private boolean isMessageContaining(String message, String substring) {
        Pattern pattern = Pattern.compile(substring, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(message);

        return matcher.find();
    }

    protected Optional<String> getCurrentUserId() {
        return Optional.ofNullable((String) this.currentRequest.getAttribute("uid"));
    }
}
