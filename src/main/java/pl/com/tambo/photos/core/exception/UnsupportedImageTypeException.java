package pl.com.tambo.photos.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE,
        reason = "Supported formats: JPEG, PNG, GIF, BMP and WBMP")
public class UnsupportedImageTypeException extends RuntimeException {
}