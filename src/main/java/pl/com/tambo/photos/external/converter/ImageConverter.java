package pl.com.tambo.photos.external.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import pl.com.tambo.photos.core.model.Image;
import pl.com.tambo.photos.external.entity.ImageEntity;
import pl.com.tambo.photos.external.entity.UserEntity;

import java.nio.file.Path;
import java.time.LocalDateTime;

@Component
public class ImageConverter {

    @Value("${upload.dir}")
    private Path uploadPath;

    public ImageEntity toEntity(Image image) {
        UserEntity user = UserEntity.builder()
                .id(image.getOwnerId())
                .build();
        return ImageEntity.builder()
                .id(image.getId())
                .filename(image.getFilename())
                .uploadTimestamp(LocalDateTime.now())
                .mediaType(image.getMediaType().toString())
                .user(user)
                .build();
    }

    public Image fromEntity(ImageEntity entity) {
        return Image.builder()
                .id(entity.getId())
                .filename(entity.getFilename())
                .uploadTimestamp(entity.getUploadTimestamp())
                .path(uploadPath)
                .mediaType(MediaType.valueOf(entity.getMediaType()))
                .ownerId(entity.getUser().getId())
                .build();
    }
}
