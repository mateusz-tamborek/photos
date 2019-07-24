package pl.com.tambo.photos.delivery.presenter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import pl.com.tambo.photos.core.model.Storable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageFilePresenter {

    public static ResponseEntity<byte[]> getResponse(Storable image) throws IOException {
        Path path = image.getPath();
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(Files.probeContentType(path)))
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + image.getFilename() + "\"")
                .body(Files.readAllBytes(path));
    }

}
