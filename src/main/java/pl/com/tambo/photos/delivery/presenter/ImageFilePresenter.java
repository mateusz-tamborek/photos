package pl.com.tambo.photos.delivery.presenter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import pl.com.tambo.photos.core.model.Media;

import java.io.IOException;
import java.nio.file.Files;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageFilePresenter {

    public static ResponseEntity<byte[]> getResponse(Media image) throws IOException {
         return ResponseEntity.ok()
                .contentType(image.getMediaType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + image.getFilename() + "\"")
                .body(Files.readAllBytes(image.getPath()));
    }

}
