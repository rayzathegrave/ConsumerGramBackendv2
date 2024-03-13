package nl.consumergram.consumergramv2.controllers;

import nl.consumergram.consumergramv2.dtos.UserDto;
import nl.consumergram.consumergramv2.exceptions.BadRequestException;
import nl.consumergram.consumergramv2.services.AuthorityService;
import nl.consumergram.consumergramv2.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;


//@CrossOrigin Geeft aan dat Cross-Origin Resource Sharing (CORS) is ingeschakeld,
@CrossOrigin
@RestController
//De @RequestMapping-annotatie geeft aan dat alle eindpunten in deze controller zullen beginnen met "/users"
@RequestMapping(value = "/users")
public class UserController {
    //Contructor om Spring bean te injecteren
    private final UserService userService;
    private final AuthorityService authorityService;

    public UserController(UserService userService, AuthorityService authorityService) {
        this.userService = userService;
        this.authorityService = authorityService;
    }

    //    HTTP GET-endpoint op het pad "/users". Het retourneert een lijst van UserDto-objecten.
    @GetMapping(value = "")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> userDtos = userService.getUsers();
        return ResponseEntity.ok().body(userDtos);
    }

    //    HTTP GET-endpoint op het pad "/users/{username}". Het retourneert een enkel UserDto-object op basis van de
//    opgegeven gebruikersnaam.
    @GetMapping(value = "/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable("username") String username) {
        UserDto optionalUser = userService.getUser(username);

        return ResponseEntity.ok().body(optionalUser);
    }

    //    HTTP POST-endpoint op het pad "/users". Het creëert een nieuwe gebruiker op basis van het meegeleverde
//    UserDto-object in het verzoek.
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto dto) {
        if (userService.userExists(dto.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        // Let op: het password van een nieuwe gebruiker wordt in deze code nog niet encrypted opgeslagen.
        // Je kan dus (nog) niet inloggen met een nieuwe user.
        String newUsername = userService.createUser(dto);
        authorityService.addAuthority(newUsername, "ROLE_USER");
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUsername).toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/admin")
    public ResponseEntity<UserDto> createAdmin(@RequestBody UserDto dto) {
        // Let op: het password van een nieuwe gebruiker wordt in deze code nog niet encrypted opgeslagen.
        // Je kan dus (nog) niet inloggen met een nieuwe user.
        String newUsername = userService.createUser(dto);
        authorityService.addAuthority(newUsername, "ROLE_ADMIN");
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUsername).toUri();
        return ResponseEntity.created(location).build();
    }



    @PutMapping(value = "/{username}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("username") String username, @RequestBody UserDto dto) {
        UserDto updatedUser = userService.updateUser(username, dto);
        return ResponseEntity.ok(updatedUser);
    }


    //    Een HTTP DELETE-endpoint op het pad "/users/{username}". Het verwijdert een gebruiker op basis van de
//    opgegeven gebruikersnaam.
    @DeleteMapping(value = "/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }


}