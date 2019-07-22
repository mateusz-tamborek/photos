package pl.com.tambo.photos.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class UnsupportedImageTypeException extends RuntimeException {

    public UnsupportedImageTypeException(String filename) {
        super("Type of file: " + filename + " is not supported. Available formats: JPEG, PNG, GIF, BMP and WBMP");
    }

}