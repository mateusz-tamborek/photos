package pl.com.tambo.photos.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException(UUID id) {
        super("Not found image with id=" + id);
    }
}
