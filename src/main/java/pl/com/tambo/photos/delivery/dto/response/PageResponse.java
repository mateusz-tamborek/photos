package pl.com.tambo.photos.delivery.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@ApiModel(value = "Page")
public class PageResponse<T> {

    @ApiModelProperty(value = "Requested content")
    private final List<T> content;

    @ApiModelProperty(value = "Total number of elements", example = "21")
    private long totalElements;

    @ApiModelProperty(value = "Total number of pages", example = "3")
    private long totalPages;
}
