package pl.com.tambo.photos.model;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.UUID;

@Getter
public class Image {

    private UUID imageId = UUID.randomUUID();
    private MultipartFile file;

    public Image(MultipartFile file) {
        validateImage(file);
        this.file = file;
    }

    public String getFilename() {
        return file.getOriginalFilename();
    }

    private void validateImage(MultipartFile file) {
        // According to https://stackoverflow.com/a/4169776
        // I don't check the content type because it operates on the file extension,
        // and the Thumbnailator lib also uses ImageIO.
        try {
            if (ImageIO.read(file.getInputStream()) == null) {
                throw new UnsupportedImageTypeException();
            }
        } catch (IOException e) {
            throw new UnsupportedImageTypeException();
        }
    }

    private class UnsupportedImageTypeException extends RuntimeException {
    }
}
