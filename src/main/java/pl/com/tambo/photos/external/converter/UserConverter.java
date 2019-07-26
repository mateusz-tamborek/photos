package pl.com.tambo.photos.external.converter;

import org.springframework.stereotype.Component;
import pl.com.tambo.photos.core.model.User;
import pl.com.tambo.photos.external.entity.UserEntity;

@Component
public class UserConverter {

    public User fromEntity(UserEntity user) {
        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }

    public UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }

}
