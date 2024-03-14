package nl.consumergram.consumergramv2.services;

import nl.consumergram.consumergramv2.dtos.UserDto;
import nl.consumergram.consumergramv2.models.User;
import nl.consumergram.consumergramv2.repositories.UserRepository;
import nl.consumergram.consumergramv2.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsers() {
        // Arrange
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setUsername("testUsername");
        userList.add(user);

        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<UserDto> result = userService.getUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals("testUsername", result.get(0).getUsername());
    }

    @Test
    void getUser() {
        // Arrange
        String username = "testUsername";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        // Act
        UserDto result = userService.getUser(username);

        // Assert
        assertEquals(username, result.getUsername());
    }

    @Test
    void getUser_UserNotFound() {
        // Arrange
        String username = "testUsername";

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.getUser(username));
    }

    @Test
    void userExists() {
        // Arrange
        String username = "testUsername";

        when(userRepository.existsById(anyString())).thenReturn(true);

        // Act
        boolean result = userService.userExists(username);

        // Assert
        assertTrue(result);
    }

    @Test
    void createUser() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("testUsername");
        userDto.setPassword("testPassword");
        userDto.setEnabled(true);

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        String result = userService.createUser(userDto);

        // Assert
        assertEquals("testUsername", result);
    }


    @Test
    void deleteUser() {
        // Arrange
        String username = "testUsername";

        // Act
        userService.deleteUser(username);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(username);
    }

    @Test
    void updateUser() {
        // Arrange
        String username = "testUsername";
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword("testPassword");

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserDto result = userService.updateUser(username, userDto);

        // Assert
        assertEquals(username, result.getUsername());
    }
}