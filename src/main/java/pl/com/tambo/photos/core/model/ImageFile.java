package pl.com.tambo.photos.core.model;

import java.nio.file.Path;

public interface ImageFile {
    Path getPath();
    String getFilename();
}
