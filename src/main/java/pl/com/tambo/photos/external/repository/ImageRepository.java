package pl.com.tambo.photos.external.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.com.tambo.photos.core.exception.ImageNotFoundException;
import pl.com.tambo.photos.core.model.Image;
import pl.com.tambo.photos.core.model.OnePage;
import pl.com.tambo.photos.core.model.User;
import pl.com.tambo.photos.external.converter.ImageConverter;
import pl.com.tambo.photos.external.converter.UserConverter;
import pl.com.tambo.photos.external.entity.ImageEntity;
import pl.com.tambo.photos.external.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

    private final JpaImageRepository repository;
    private final ImageConverter imageConverter;
    private final UserConverter userConverter;

    public Image findBy(UUID id, User user) {
        ImageEntity entity = getImageEntity(id, user);
        return imageConverter.fromEntity(entity);
    }

    public OnePage<Image> findByNameContaining(String name, User user, Pageable pageable) {
        UserEntity userEntity = userConverter.toEntity(user);
        Page<ImageEntity> page = repository.findAllByUserAndFilenameIgnoreCaseContaining(userEntity, name, pageable);
        List<Image> content = page.map(imageConverter::fromEntity).getContent();
        return OnePage.<Image>builder()
                .content(content)
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    public Image save(Image image) {
        ImageEntity persisted = repository.save(imageConverter.toEntity(image));
        return imageConverter.fromEntity(persisted);
    }

    public void deleteBy(UUID id, User user) {
        ImageEntity entity = getImageEntity(id, user);
        repository.delete(entity);
    }

    private ImageEntity getImageEntity(UUID id, User user) {
        UserEntity userEntity = userConverter.toEntity(user);
        return repository.findByUserAndId(userEntity, id)
                .orElseThrow(() -> new ImageNotFoundException(id));
    }

}

interface JpaImageRepository extends PagingAndSortingRepository<ImageEntity, UUID> {
    Optional<ImageEntity> findByUserAndId(UserEntity user, UUID id);
    Page<ImageEntity> findAllByUserAndFilenameIgnoreCaseContaining(UserEntity user, String filename, Pageable pageable);
}

