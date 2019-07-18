package pl.com.tambo.photos.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.com.tambo.photos.entity.ImageEntity;

import java.util.UUID;

@Repository
public interface ImageRepository extends PagingAndSortingRepository<ImageEntity, UUID> {

}
