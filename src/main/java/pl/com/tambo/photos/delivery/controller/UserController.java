package pl.com.tambo.photos.delivery.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.tambo.photos.core.logic.UserService;
import pl.com.tambo.photos.core.model.User;
import pl.com.tambo.photos.delivery.dto.request.AuthenticationRequest;

import java.net.URI;

@RestController
@Api(tags = "User")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createUser(@RequestBody AuthenticationRequest data) {
        User user = userService.createUser(data);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

}
