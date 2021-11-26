package pl.kj.bachelors.teams.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import pl.kj.bachelors.teams.domain.model.entity.UploadedFile;

@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Integer> {
    @Override
    @NonNull
    <S extends UploadedFile> S save(@NonNull S entity);

}
