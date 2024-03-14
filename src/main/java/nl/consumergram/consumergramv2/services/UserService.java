

package nl.consumergram.consumergramv2.services;

import jakarta.persistence.EntityNotFoundException;
import nl.consumergram.consumergramv2.dtos.UserDto;
import nl.consumergram.consumergramv2.models.User;
import nl.consumergram.consumergramv2.repositories.UserRepository;
import nl.consumergram.consumergramv2.utils.RandomStringGenerator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public PasswordEncoder passwordEncoder;

    //    De constructor injecteert een instantie van UserRepository
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//        // Opslaan van het versleutelde wachtwoord in de database of ergens anders
//        userRepository.save(new User(UserDto.getUsername(), encodedPassword));
//    }


    public List<UserDto> getUsers() {
        List<UserDto> collection = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user : list) {
            collection.add(fromUser(user));
        }
        return collection;
    }

    //    Haalt een specifieke gebruiker op basis van de gebruikersnaam en converteert deze naar een UserDto
    public UserDto getUser(String username) {
        UserDto dto = new UserDto();
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            dto = fromUser(user.get());
        } else {
            throw new UsernameNotFoundException(username);
        }
        return dto;
    }

    //    Controleert of een gebruiker met de opgegeven gebruikersnaam bestaat.
    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    //    Genereert een willekeurige API-sleutel, stelt deze in op de UserDto en slaat een nieuwe gebruiker op in de database. Geeft de gebruikersnaam van de aangemaakte gebruiker terug.
    public String createUser(UserDto userDto) {
        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        userDto.setApikey(randomString);
        User newUser = userRepository.save(toUser(userDto));
        return newUser.getUsername();
    }

    //    Verwijdert een gebruiker op basis van de gebruikersnaam.
    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public UserDto updateUser(String username, UserDto dto) {
        // Fetch the user from the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username " + username));

        // Update the user fields
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // ... update other fields as necessary

        // Save the updated user back to the database
        User updatedUser = userRepository.save(user);

        // Convert the updated User entity to UserDto and return it
        UserDto updatedUserDto = convertToDto(updatedUser);
        return updatedUserDto;
    }

    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setEnabled(user.isEnabled());
        userDto.setApikey(user.getApikey());
        userDto.setEmail(user.getEmail());
        userDto.setAuthorities(user.getAuthorities());
        return userDto;
    }


    //    Zet een User-object om naar een UserDto
    public static UserDto fromUser(User user) {

        var dto = new UserDto();

//        In de code lijkt getUsername() de
//        gebruikersnaam terug te geven, die vaak wordt gebruikt als een unieke
//        identificator voor een gebruiker.

        dto.username = user.getUsername();
        dto.password = user.getPassword();
        dto.enabled = user.isEnabled();
        dto.apikey = user.getApikey();
        dto.email = user.getEmail();
        dto.authorities = user.getAuthorities();

        return dto;
    }

    //    Zet een UserDto-object om naar een User.
    public User toUser(UserDto userDto) {
        var user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.password));
//        user.setPassword(userDto.getPassword());
        user.setEnabled(userDto.getEnabled());
        user.setApikey(userDto.getApikey());
        user.setEmail(userDto.getEmail());
        return user;
    }

}