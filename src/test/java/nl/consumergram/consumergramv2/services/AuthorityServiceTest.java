package nl.consumergram.consumergramv2.services;

import nl.consumergram.consumergramv2.models.Authority;
import nl.consumergram.consumergramv2.models.User;
import nl.consumergram.consumergramv2.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AuthorityServiceTest {

    @InjectMocks
    AuthorityService authorityService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAuthorities() {
        // Arrange
        String username = "testUsername";
        Authority authority = new Authority();
        authority.setAuthority("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);

        User user = new User();
        user.setUsername(username);
        user.setAuthorities(authorities);

        when(userRepository.existsById(anyString())).thenReturn(true);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        // Act
        Set<Authority> result = authorityService.getAuthorities(username);

        // Assert
        assertEquals(1, result.size());
        assertEquals("ROLE_USER", result.iterator().next().getAuthority());
    }

    @Test
    void addAuthority() {
        // Arrange
        String username = "testUsername";
        String authorityName = "ROLE_ADMIN";

        User user = new User();
        user.setUsername(username);

        when(userRepository.existsById(anyString())).thenReturn(true);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        // Act
        authorityService.addAuthority(username, authorityName);

        // Assert
        assertEquals(1, user.getAuthorities().size());
        assertEquals(authorityName, user.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void removeAuthority() {
        // Arrange
        String username = "testUsername";
        String authorityName = "ROLE_USER";

        Authority authority = new Authority();
        authority.setAuthority(authorityName);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);

        User user = new User();
        user.setUsername(username);
        user.setAuthorities(authorities);

        when(userRepository.existsById(anyString())).thenReturn(true);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        // Act
        authorityService.removeAuthority(username, authorityName);

        // Assert
        assertEquals(0, user.getAuthorities().size());
    }

    @Test
    void getAuthorities_UserNotFound() {
        // Arrange
        String username = "testUsername";

        when(userRepository.existsById(anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authorityService.getAuthorities(username));
    }

    @Test
    void addAuthority_UserNotFound() {
        // Arrange
        String username = "testUsername";
        String authorityName = "ROLE_ADMIN";

        when(userRepository.existsById(anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authorityService.addAuthority(username, authorityName));
    }

    @Test
    void removeAuthority_UserNotFound() {
        // Arrange
        String username = "testUsername";
        String authorityName = "ROLE_USER";

        when(userRepository.existsById(anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authorityService.removeAuthority(username, authorityName));
    }
}