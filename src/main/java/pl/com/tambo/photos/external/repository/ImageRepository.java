package pl.com.tambo.photos.external.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.com.tambo.photos.core.model.Image;
import pl.com.tambo.photos.external.entity.ImageEntity;

import javax.persistence.EntityNotFoundException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

    @Value("${upload.dir}")
    private Path uploadPath;
    private final JpaImageRepository repository;

    public List<Image> findAll(Pageable pageable) {
        Page<ImageEntity> page = repository.findAll(pageable);
        return page.map(this::fromEntity)
                .getContent();
    }

    public Image findBy(UUID id) {
        ImageEntity entity = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return fromEntity(entity);
    }

    public List<Image> findByNameContaining(String name, Pageable pageable) {
        Page<ImageEntity> page = repository.findAllByFilenameIgnoreCaseContaining(name, pageable);
        return page.map(this::fromEntity)
                .getContent();
    }

    public Image save(Image image) {
        ImageEntity persisted = repository.save(toEntity(image));
        return fromEntity(persisted);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    private ImageEntity toEntity(Image image) {
        return ImageEntity.builder()
                .id(image.getId())
                .filename(image.getFilename())
                .uploadTimestamp(LocalDateTime.now())
                .build();
    }

    private Image fromEntity(ImageEntity entity) {
        return Image.builder()
                .id(entity.getId())
                .filename(entity.getFilename())
                .uploadTimestamp(entity.getUploadTimestamp())
                .path(uploadPath)
                .build();
    }

}

@Repository
interface JpaImageRepository extends PagingAndSortingRepository<ImageEntity, UUID> {
    Page<ImageEntity> findAllByFilenameIgnoreCaseContaining(String filename, Pageable pageable);
}

