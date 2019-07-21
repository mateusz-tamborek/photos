package pl.com.tambo.photos.delivery.presenter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.com.tambo.photos.core.model.Image;
import pl.com.tambo.photos.delivery.dto.ImageDTO;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageDtoPresenter {

    public static ImageDTO getResponse(Image image) {
        return ImageDTO.builder()
                .id(image.getId().toString())
                .filename(image.getFilename())
                .uploadTimestamp(getTimestamp(image.getUploadTimestamp()))
                .build();
    }

    public static List<ImageDTO> getResponse(List<Image> images) {
        return images.stream()
                .map(ImageDtoPresenter::getResponse)
                .collect(Collectors.toList());
    }

    private static long getTimestamp(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

}
