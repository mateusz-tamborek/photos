package pl.com.tambo.photos.external.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.tambo.photos.core.model.Image;
import pl.com.tambo.photos.external.entity.ImageEntity;

import javax.persistence.EntityNotFoundException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

    @Value("${upload.dir}")
    private Path uploadPath;
    private final JpaImageRepository repository;

    public Image save(Image image) {
        ImageEntity persisted = repository.save(toEntity(image));
        return fromEntity(persisted);
    }

    public Image findBy(UUID id) {
        ImageEntity entity = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return fromEntity(entity);
    }

    private ImageEntity toEntity(Image image) {
        return ImageEntity.builder()
                .id(image.getId())
                .originalFilename(image.getFilename())
                .uploadTimestamp(LocalDateTime.now())
                .build();
    }

    private Image fromEntity(ImageEntity entity) {
        return Image.builder()
                .id(entity.getId())
                .filename(entity.getOriginalFilename())
                .uploadTimestamp(entity.getUploadTimestamp())
                .path(uploadPath)
                .build();
    }

}

@Repository
@Transactional
interface JpaImageRepository extends PagingAndSortingRepository<ImageEntity, UUID> {
}