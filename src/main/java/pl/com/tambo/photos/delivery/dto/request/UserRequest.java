package pl.com.tambo.photos.delivery.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@ApiModel
public class UserRequest {

    @JsonIgnore
    private Long id;

    @ApiModelProperty(required = true, example = "user@domain.com")
    @Email
    private String email;

    @ApiModelProperty(required = true, example = "Password123!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$",
            message = "Password must contain minimum 8 and maximum 20 characters, at least one uppercase letter, " +
                    "one lowercase letter, one number and one special character (!@#$%^&*)")
    private String password;

}