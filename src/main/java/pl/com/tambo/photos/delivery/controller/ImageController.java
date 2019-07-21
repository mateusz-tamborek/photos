package pl.com.tambo.photos.delivery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.com.tambo.photos.core.logic.ImageService;
import pl.com.tambo.photos.core.logic.UploadRequest;
import pl.com.tambo.photos.core.model.Image;
import pl.com.tambo.photos.delivery.dto.ImageDTO;
import pl.com.tambo.photos.delivery.presenter.ImageDtoPresenter;
import pl.com.tambo.photos.delivery.presenter.ImagePresenter;

import java.io.IOException;
import java.util.UUID;

@Api(tags = "Images")
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ImageDTO handleFileUpload(
            @ApiParam(value = "UploadRequest file. Supported formats: JPEG, PNG, GIF, BMP and WBMP", required = true)
            @RequestParam("file") MultipartFile file) {
        UploadRequest uploadRequest = new UploadRequest(file);
        Image image = imageService.save(uploadRequest);
        return ImageDtoPresenter.getResponse(image);
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(
            @ApiParam(value = "FullImage identifier", required = true)
            @PathVariable(name = "id") UUID id) throws IOException {
        Image image = imageService.findBy(id);
        return ImagePresenter.getResponse(image);
    }

    @GetMapping(value = "/thumbnails/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getThumbnail(
            @ApiParam(value = "FullImage identifier", required = true)
            @PathVariable(name = "id") UUID id) throws IOException {
        Image image = imageService.findBy(id);
        return ImagePresenter.getResponse(image.getThumbnail());
    }

}
