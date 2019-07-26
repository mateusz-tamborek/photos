package pl.com.tambo.photos.core.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.tambo.photos.config.security.JwtTokenProvider;
import pl.com.tambo.photos.core.exception.AuthenticationException;
import pl.com.tambo.photos.core.exception.UserAlreadyExistsException;
import pl.com.tambo.photos.core.model.User;
import pl.com.tambo.photos.delivery.dto.request.AuthenticationRequest;
import pl.com.tambo.photos.delivery.dto.request.UserRequest;
import pl.com.tambo.photos.external.repository.UserRepository;

import java.util.Collections;

import static com.google.common.base.MoreObjects.firstNonNull;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public User createUser(UserRequest request) {
        checkEmail(request);
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        return userRepository.save(user);
    }

    public void updateUser(UserRequest request, User currentUser) {
        if (!request.getId().equals(currentUser.getId())) {
            throw new AuthenticationException("Unauthorized");
        }
        checkEmail(request);
        User user = userRepository.findById(request.getId());
        user.setEmail(firstNonNull(request.getEmail(), user.getEmail()));
        String password = request.getPassword() != null ? passwordEncoder.encode(request.getPassword()) : null;
        user.setPassword(firstNonNull(password, user.getPassword()));
        userRepository.save(user);
    }

    public String createToken(AuthenticationRequest request) {
        try {
            String username = request.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));
            User user = userRepository.loadUserByUsername(username);
            return jwtTokenProvider.createToken(user);
        } catch (Exception e) {
            throw new AuthenticationException("Invalid email/password supplied");
        }
    }

    private void checkEmail(UserRequest request) {
        if (userRepository.emailExists(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }
    }

}
