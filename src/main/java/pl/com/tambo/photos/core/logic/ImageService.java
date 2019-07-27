package pl.com.tambo.photos.core.logic;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import pl.com.tambo.photos.core.exception.StoreImageException;
import pl.com.tambo.photos.core.model.Image;
import pl.com.tambo.photos.core.model.Image.Thumbnail;
import pl.com.tambo.photos.core.model.ImageFile;
import pl.com.tambo.photos.core.model.OnePage;
import pl.com.tambo.photos.core.model.User;
import pl.com.tambo.photos.external.repository.ImageRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final Path uploadPath;
    private final Tika mimeTypeDetector = new Tika();

    ImageService(ImageRepository imageRepository, @Value("${upload.dir}") Path uploadPath) throws IOException {
        this.imageRepository = imageRepository;
        this.uploadPath = uploadPath.toAbsolutePath();
        Path thumbnailPath = this.uploadPath.resolve(Thumbnail.DIRECTORY);
        log.debug("Create directories for images at: {} and {}", this.uploadPath, thumbnailPath);
        Files.createDirectories(thumbnailPath);
    }

    public Image save(ImageFile imageFile, User user) {
        Image image = getImage(imageFile, user);
        try {
            createAndStoreThumbnail(imageFile, image.getThumbnail());
            storeOriginalImage(imageFile, image);
            return imageRepository.save(image);
        } catch (Exception cleanupEx) {
            try {
                Path thumbnail = image.getThumbnail().getPath();
                Files.deleteIfExists(thumbnail);
                Path fullImage = image.getPath();
                Files.deleteIfExists(fullImage);
            } catch (IOException deleteEx) {
                throw new StoreImageException(deleteEx.initCause(cleanupEx));
            }
            throw new StoreImageException(cleanupEx);
        }
    }

    public Image findBy(UUID id, User user) {
        return imageRepository.findBy(id, user);
    }

    public OnePage<Image> findByName(String name, User user, Pageable pageable) {
        return imageRepository.findByNameContaining(name, user, pageable);
    }

    public void delete(UUID id, User user) {
        imageRepository.deleteBy(id, user);
    }

    private Image getImage(ImageFile imageFile, User user) {
        return Image.builder()
                .id(imageFile.getId())
                .filename(imageFile.name())
                .path(uploadPath)
                .ownerId(user.getId())
                .mediaType(getMediaType(imageFile))
                .build();
    }

    private MediaType getMediaType(ImageFile imageFile) {
        try {
            return MediaType.valueOf(mimeTypeDetector.detect(imageFile.getFile().getBytes()));
        } catch (IOException e) {
            throw new StoreImageException(e);
        }
    }

    private void storeOriginalImage(ImageFile imageFile, Image image) throws IOException {
        imageFile.getFile().transferTo(image.getPath());
    }

    private void createAndStoreThumbnail(ImageFile imageFile, Thumbnail image) throws IOException {
        BufferedImage thumbnail = Thumbnails.of(imageFile.getFile().getInputStream())
                .size(Thumbnail.SIZE, Thumbnail.SIZE)
                .asBufferedImage();
        ImageIO.write(thumbnail, Thumbnail.FORMAT, image.getPath().toFile());
    }

}
