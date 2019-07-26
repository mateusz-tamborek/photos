package pl.com.tambo.photos.external.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pl.com.tambo.photos.core.exception.UserNotFoundException;
import pl.com.tambo.photos.core.model.User;
import pl.com.tambo.photos.external.converter.UserConverter;
import pl.com.tambo.photos.external.entity.UserEntity;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepository implements UserDetailsService {

    private final JpaUserRepository users;
    private final UserConverter converter;

    public User save(User newUser) {
        UserEntity entity = users.saveAndFlush(converter.toEntity(newUser));
        return converter.fromEntity(entity);
    }

    @Override
    public User loadUserByUsername(String email) {
        return users.findByEmail(email)
                .map(converter::fromEntity)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
    }

    public boolean emailExists(String email) {
        return users.existsByEmail(email);
    }

    public User findById(long id) {
        return converter.fromEntity(users.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found")));
    }

}

@Repository
interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Boolean existsByEmail(String email);
}