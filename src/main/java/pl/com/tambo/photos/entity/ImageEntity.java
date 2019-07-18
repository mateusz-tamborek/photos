package pl.com.tambo.photos.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity(name = "image")
public class ImageEntity {

    @Id
    private UUID id;

    private String originalFilename;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            nullable = false,
            insertable = false,
            updatable = false)
    private LocalDateTime uploadTimestamp;

    @Tolerate
    ImageEntity() {
        // required by JPA
    }

}
