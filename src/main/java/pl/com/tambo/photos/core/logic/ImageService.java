package pl.com.tambo.photos.core.logic;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.com.tambo.photos.core.exception.StoreImageException;
import pl.com.tambo.photos.core.model.Image;
import pl.com.tambo.photos.core.model.Image.Thumbnail;
import pl.com.tambo.photos.external.repository.ImageRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final Path uploadPath;

    ImageService(ImageRepository imageRepository, @Value("${upload.dir}") Path uploadPath) throws IOException {
        this.imageRepository = imageRepository;
        this.uploadPath = uploadPath;
        Path thumbnailPath = uploadPath.resolve(Thumbnail.DIRECTORY);
        Files.createDirectories(thumbnailPath);
    }

    public Image save(ImageFile imageFile) {
        Image image = Image.builder()
                .id(imageFile.getId())
                .filename(imageFile.name())
                .path(uploadPath)
                .build();
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

    public Image findBy(UUID id) {
        return imageRepository.findBy(id);
    }

    public List<Image> findAll(Pageable pageable) {
        return imageRepository.findAll(pageable);
    }

    public List<Image> findByName(String name, Pageable pageable) {
        return imageRepository.findByNameContaining(name, pageable);
    }

    public void delete(UUID id) {
        imageRepository.delete(id);
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
