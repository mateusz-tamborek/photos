package pl.com.tambo.photos.core.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class OnePage<T> {
    private List<T> content;
    private long totalElements;
    private long totalPages;
}
