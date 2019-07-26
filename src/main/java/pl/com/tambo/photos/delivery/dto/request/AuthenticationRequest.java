package pl.com.tambo.photos.delivery.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class AuthenticationRequest {

    @ApiModelProperty(required = true, example = "user@domain.com")
    @Email
    @NotBlank
    private String email;

    @ApiModelProperty(required = true, example = "Password123!")
    @NotBlank
    private String password;

}