package pl.com.tambo.photos.core.model;

import org.springframework.http.MediaType;

public interface Media extends Storable {
    MediaType getMediaType();
}
