package pl.com.tambo.photos.delivery.presenter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.com.tambo.photos.core.model.Image;
import pl.com.tambo.photos.delivery.dto.response.ImageResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImagePresenter {

    public static ImageResponse getResponse(Image image) {
        return ImageResponse.builder()
                .id(image.getId().toString())
                .filename(image.getFilename())
                .uploadTimestamp(getTimestamp(image.getUploadTimestamp()))
                .build();
    }

    public static List<ImageResponse> getResponse(List<Image> images) {
        return images.stream()
                .map(ImagePresenter::getResponse)
                .collect(Collectors.toList());
    }

    private static long getTimestamp(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

}
