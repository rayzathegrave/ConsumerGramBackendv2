package nl.consumergram.consumergramv2.controllers;

import nl.consumergram.consumergramv2.dtos.AuthenticationRequest;
import nl.consumergram.consumergramv2.dtos.AuthenticationResponse;
import nl.consumergram.consumergramv2.services.CustomUserDetailsService;
import nl.consumergram.consumergramv2.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

//@CrossOrigin Geeft aan dat Cross-Origin Resource Sharing (CORS) is ingeschakeld,
@CrossOrigin
@RestController
public class AuthenticationController {

    //Contructor om Spring bean te injecteren

    private final AuthenticationManager authenticationManager;
    //Contructor om Spring bean te injecteren
    private final CustomUserDetailsService userDetailsService;

    //Contructor om Spring bean te injecteren
    private final JwtUtil jwtUtl;

    public AuthenticationController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtUtl) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtl = jwtUtl;
    }


    //Deze methode geeft de principal (basis user gegevens) terug van de ingelogde gebruiker
    @GetMapping(value = "/authenticated")
    public ResponseEntity<Object> authenticated(Authentication authentication, Principal principal) {
        return ResponseEntity.ok().body(principal);
    }


    //Deze methode geeft het JWT token terug wanneer de gebruiker de juiste inloggegevens op geeft.
    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        //De methode probeert de authenticatie uit te voeren met de ingevoerde inloggegevens.
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        }
        //Deze code retourneert een BadCredentialsException wanneer de authenticatie niet lukt.
        catch (BadCredentialsException ex) {
            throw new Exception("Incorrect username or password", ex);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(username);

        final String jwt = jwtUtl.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}