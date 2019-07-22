package pl.com.tambo.photos.core.logic;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import pl.com.tambo.photos.core.exception.UnsupportedImageTypeException;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.UUID;

@Getter
public class UploadRequest {

    private final UUID id = UUID.randomUUID();
    private final MultipartFile file;

    public UploadRequest(MultipartFile file) {
        validateImage(file);
        this.file = file;
    }

    String getFilename() {
        return file.getOriginalFilename();
    }

    private void validateImage(MultipartFile file) {
        // According to https://stackoverflow.com/a/4169776
        // I don't check the content type because it operates on the file extension,
        // and the Thumbnailator lib also uses ImageIO.
        try {
            if (ImageIO.read(file.getInputStream()) == null) {
                throw new UnsupportedImageTypeException(file.getOriginalFilename());
            }
        } catch (IOException e) {
            throw new UnsupportedImageTypeException(file.getOriginalFilename());
        }
    }
}
