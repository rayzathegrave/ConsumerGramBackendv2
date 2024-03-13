package nl.consumergram.consumergramv2.controllers;


import nl.consumergram.consumergramv2.exceptions.BadRequestException;
import nl.consumergram.consumergramv2.services.AuthorityService;
import nl.consumergram.consumergramv2.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
//De @RequestMapping-annotatie geeft aan dat alle eindpunten in deze controller zullen beginnen met "/users"
@RequestMapping(value = "/users")
public class AuthorityController {

    private final AuthorityService authorityService;

    public AuthorityController( AuthorityService authorityService) {

        this.authorityService = authorityService;
    }

    @GetMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> getUserAuthorities(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(authorityService.getAuthorities(username));
    }

    //    Een HTTP POST-endpoint op het pad "/users/{username}/authorities". Het voegt een nieuwe autoriteit
//    toe aan een gebruiker op basis van de opgegeven gebruikersnaam en de autoriteit in het verzoek
    @PostMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> addUserAuthority(@PathVariable("username") String username, @RequestBody Map<String, Object> fields) {
        try {
            String authorityName = (String) fields.get("authority");
            authorityService.addAuthority(username, authorityName);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            throw new BadRequestException();
        }
    }

    //    Een HTTP DELETE-endpoint op het pad "/users/{username}/authorities/{authority}". Het verwijdert een
//    specifieke autoriteit van een gebruiker op basis van de opgegeven gebruikersnaam en autoriteit.
    @DeleteMapping(value = "/{username}/authorities/{authority}")
    public ResponseEntity<Object> deleteUserAuthority(@PathVariable("username") String username, @PathVariable("authority") String authority) {
        authorityService.removeAuthority(username, authority);
        return ResponseEntity.noContent().build();
    }


}
