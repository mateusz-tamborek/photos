package pl.com.tambo.photos.external.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pl.com.tambo.photos.core.exception.UserAlreadyExistsException;
import pl.com.tambo.photos.core.exception.UserNotFoundException;
import pl.com.tambo.photos.core.model.User;
import pl.com.tambo.photos.external.entity.UserEntity;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepository implements UserDetailsService {

    private final JpaUserRepository users;

    public User save(User newUser) {
        if(users.existsByEmail(newUser.getEmail())) {
            throw new UserAlreadyExistsException(newUser.getEmail());
        }
        UserEntity entity = users.saveAndFlush(toEntity(newUser));
        return fromEntity(entity);
    }

    @Override
    public User loadUserByUsername(String email) {
        return users.findByEmail(email)
                .map(this::fromEntity)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    private User fromEntity(UserEntity user) {
        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }

    private UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }
}

@Repository
interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Boolean existsByEmail(String email);
}