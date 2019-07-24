package pl.com.tambo.photos.delivery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.tambo.photos.core.logic.ImageFile;
import pl.com.tambo.photos.core.logic.ImageService;
import pl.com.tambo.photos.core.model.Image;
import pl.com.tambo.photos.delivery.ApiPageable;
import pl.com.tambo.photos.delivery.dto.response.ImageResponse;
import pl.com.tambo.photos.delivery.presenter.ImageFilePresenter;
import pl.com.tambo.photos.delivery.presenter.ImagePresenter;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Api(tags = "Images")
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ImageResponse> handleFileUpload(
            @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "Image file. Supported formats: JPEG, PNG, GIF, BMP and WBMP", required = true)
            @RequestParam("file") MultipartFile file) {
        ImageFile imageFile = new ImageFile(file);
        Image image = imageService.save(imageFile);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(image.getId()).toUri();
        return ResponseEntity.created(location)
                .body(ImagePresenter.getResponse(image));
    }

    @GetMapping(value = "/images", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiPageable
    public List<ImageResponse> getImagesInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable,
            @ApiParam(value = "Searching by filename. Works as LIKE %name%")
            @RequestParam(name = "name", required = false) String name) {
        List<Image> images = name == null ? imageService.findAll(pageable) : imageService.findByName(name, pageable);
        return ImagePresenter.getResponse(images);
    }

    @DeleteMapping("/images/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(
            @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "Image identifier", required = true)
            @PathVariable(name = "id") UUID id) {
        imageService.delete(id);
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(
            @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "Image identifier", required = true)
            @PathVariable(name = "id") UUID id) throws IOException {
        Image image = imageService.findBy(id);
        return ImageFilePresenter.getResponse(image);
    }

    @GetMapping(value = "/thumbnails/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getThumbnail(
            @AuthenticationPrincipal UserDetails userDetails,
            @ApiParam(value = "Image identifier", required = true)
            @PathVariable(name = "id") UUID id) throws IOException {
        Image image = imageService.findBy(id);
        return ImageFilePresenter.getResponse(image.getThumbnail());
    }

}
