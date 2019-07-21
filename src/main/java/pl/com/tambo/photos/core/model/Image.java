package pl.com.tambo.photos.core.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@Getter
public class Image implements ImageFile {

    private final UUID id;
    private final String filename;
    private final LocalDateTime uploadTimestamp;
    private final Path path;
    private final Thumbnail thumbnail = new Thumbnail();

    public Path getPath() {
        return path.resolve(id.toString());
    }


    public class Thumbnail implements ImageFile {

        public static final short SIZE = 50;
        public static final String FORMAT = "png";
        public static final String DIRECTORY = "thumbnails";

        public Path getPath() {
            return path.resolve(Thumbnail.DIRECTORY).resolve(id.toString());
        }

        public String getFilename() {
            return "thumb-" + StringUtils.stripFilenameExtension(filename) + "." + Thumbnail.FORMAT;
        }
    }

}