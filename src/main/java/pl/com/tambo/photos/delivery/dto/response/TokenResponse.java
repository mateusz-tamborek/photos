package pl.com.tambo.photos.delivery.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ApiModel(value = "Token")
public class TokenResponse {

    @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YW1ibyIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE1NjM5ODAxMDIsImV4cCI6MTU2Mzk4MzcwMn0.OibWI0yUzB_x_kr29-igMXq2NNbxVC30FbsymJaPVzw")
    private final String token;

}