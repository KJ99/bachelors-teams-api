package pl.kj.bachelors.teams.domain.service.file;

import pl.kj.bachelors.teams.domain.model.entity.UploadedFile;

public interface FileReader {
    byte[] readFile(UploadedFile uploadedFile);
}
