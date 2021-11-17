package pl.kj.bachelors.teams.domain.service.file;

import org.springframework.web.multipart.MultipartFile;
import pl.kj.bachelors.teams.domain.exception.AggregatedApiError;
import pl.kj.bachelors.teams.domain.model.entity.UploadedFile;

import java.io.IOException;

public interface FileUploader {

    UploadedFile processUpload(
            final MultipartFile file,
            final String[] allowedMediaTypes,
            final long maxFileSize
    ) throws IOException, AggregatedApiError;
}
