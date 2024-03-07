package nl.consumergram.consumergramv2.services;

import nl.consumergram.consumergramv2.dtos.UserDto;
import nl.consumergram.consumergramv2.models.Authority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTest {

    @InjectMocks
    CustomUserDetailsService customUserDetailsService;

    @Mock
    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername() {
        // Arrange
        var username = "testUsername";
        var password = "testPassword";
        Authority authority = new Authority();
        authority.setAuthority("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setAuthorities(authorities);

        when(userService.getUser(anyString())).thenReturn(userDto);

        // Act
        var result = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertEquals(1, result.getAuthorities().size());
        assertEquals("ROLE_USER", result.getAuthorities().iterator().next().getAuthority());
    }
}