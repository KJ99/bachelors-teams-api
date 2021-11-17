package pl.kj.bachelors.teams.domain.service;

import pl.kj.bachelors.teams.domain.exception.ApiError;

import java.util.Collection;

public interface ModelValidator {
    <T> Collection<ApiError> validateModel(T model);
}
