package pl.com.tambo.photos.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tambo.photos.entity.ImageEntity;
import pl.com.tambo.photos.model.Image;
import pl.com.tambo.photos.repository.ImageRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;

import static java.nio.file.Files.*;

@Service
public class ImageService {

    private ImageRepository imageRepository;
    private String uploadPath;

    private static final String THUMBNAIL_PREFIX = "thumb-";
    private static final String THUMBNAIL_FORMAT = "png";
    private static final short THUMBNAIL_SIZE = 50;

    ImageService(ImageRepository imageRepository, @Value("${upload.dir}") Path uploadDir) throws IOException {
        this.imageRepository = imageRepository;
        if (notExists(uploadDir)) {
            createDirectory(uploadDir);
        } else if (!isDirectory(uploadDir)) {
            throw new NotDirectoryException(uploadDir.toAbsolutePath() + " is not a directory");
        }
        this.uploadPath = uploadDir.toAbsolutePath().toString();
    }

    public void save(Image image) {
        try {
            createAndStoreThumbnail(image);
            storeOriginalImage(image);
            ImageEntity entity = ImageEntity.builder()
                    .id(image.getImageId())
                    .originalFilename(image.getFilename())
                    .build();
            imageRepository.save(entity);
        } catch (IOException cleanupEx) {
            try {
                Path thumbnail = getThumbnailDestinationFile(image).toPath();
                Files.deleteIfExists(thumbnail);
                Path fullImage = getDestinationFile(image).toPath();
                Files.deleteIfExists(fullImage);
            } catch (IOException deleteEx) {
                throw new StoreImageException(deleteEx.initCause(cleanupEx));
            }
            throw new StoreImageException(cleanupEx);
        }
    }

    private void storeOriginalImage(Image image) throws IOException {
        File destination = getDestinationFile(image);
        image.getFile().transferTo(destination);
    }

    private void createAndStoreThumbnail(Image image) throws IOException {
        BufferedImage thumbnail = Thumbnails.of(image.getFile().getInputStream())
                .size(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                .asBufferedImage();
        File destination = getThumbnailDestinationFile(image);
        ImageIO.write(thumbnail, THUMBNAIL_FORMAT, destination);
    }

    private File getThumbnailDestinationFile(Image image) {
        return new File(uploadPath, THUMBNAIL_PREFIX + image.getImageId());
    }

    private File getDestinationFile(Image image) {
        return new File(uploadPath, image.getImageId().toString());
    }

    private class StoreImageException extends RuntimeException {
        private StoreImageException(Throwable e) {
            super(e);
        }
    }

}
