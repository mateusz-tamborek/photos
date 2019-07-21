package pl.com.tambo.photos.delivery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.com.tambo.photos.core.logic.ImageService;
import pl.com.tambo.photos.core.logic.UploadRequest;
import pl.com.tambo.photos.core.model.Image;
import pl.com.tambo.photos.delivery.ApiPageable;
import pl.com.tambo.photos.delivery.dto.ImageDTO;
import pl.com.tambo.photos.delivery.presenter.ImageDtoPresenter;
import pl.com.tambo.photos.delivery.presenter.ImagePresenter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Api(tags = "Images")
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ImageDTO handleFileUpload(
            @ApiParam(value = "Image file. Supported formats: JPEG, PNG, GIF, BMP and WBMP", required = true)
            @RequestParam("file") MultipartFile file) {
        UploadRequest uploadRequest = new UploadRequest(file);
        Image image = imageService.save(uploadRequest);
        return ImageDtoPresenter.getResponse(image);
    }

    @GetMapping(value = "/images", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiPageable
    public List<ImageDTO> listImages(Pageable pageable) {
        List<Image> images = imageService.findAll(pageable);
        return ImageDtoPresenter.getResponse(images);
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(
            @ApiParam(value = "Image identifier", required = true)
            @PathVariable(name = "id") UUID id) throws IOException {
        Image image = imageService.findBy(id);
        return ImagePresenter.getResponse(image);
    }

    @GetMapping(value = "/thumbnails/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getThumbnail(
            @ApiParam(value = "Image identifier", required = true)
            @PathVariable(name = "id") UUID id) throws IOException {
        Image image = imageService.findBy(id);
        return ImagePresenter.getResponse(image.getThumbnail());
    }

}
