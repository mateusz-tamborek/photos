package pl.com.tambo.photos.delivery.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AuthenticationRequest {

    @ApiModelProperty(required = true, example = "user@domain.com")
    private String email;

    @ApiModelProperty(required = true, example = "password123")
    private String password;
}