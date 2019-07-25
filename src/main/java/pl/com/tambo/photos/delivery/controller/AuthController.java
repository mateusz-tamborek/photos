package pl.com.tambo.photos.delivery.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tambo.photos.core.logic.UserService;
import pl.com.tambo.photos.delivery.dto.request.AuthenticationRequest;
import pl.com.tambo.photos.delivery.dto.response.TokenResponse;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Api(tags = "Auth")
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping(value = "/token", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public TokenResponse signIn(@RequestBody @Valid AuthenticationRequest credentials) {
        String token = userService.createToken(credentials);
        return new TokenResponse(token);
    }

}