package pl.com.tambo.photos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.com.tambo.photos.model.Image;
import pl.com.tambo.photos.service.ImageService;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void handleFileUpload(@RequestParam("file") MultipartFile file) {
        Image image = new Image(file);
        imageService.save(image);
    }

}
