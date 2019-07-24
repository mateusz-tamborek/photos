package pl.com.tambo.photos.core.model;

import java.nio.file.Path;

public interface Storable {
    Path getPath();
    String getFilename();
}
