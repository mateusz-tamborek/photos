package pl.com.tambo.photos.delivery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@ApiModel(value = "FullImage")
public class ImageDTO {

    @ApiModelProperty(example = "0c3247b8-a3b1-4762-b195-344d890b270f")
    private String id;

    @ApiModelProperty(value = "Original name of uploaded file", example = "image.jpg")
    private String filename;

    @ApiModelProperty(value = "UNIX timestamp in seconds", example = "654919200")
    private long uploadTimestamp;

}


