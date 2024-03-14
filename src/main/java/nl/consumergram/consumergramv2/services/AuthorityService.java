package nl.consumergram.consumergramv2.services;

import nl.consumergram.consumergramv2.dtos.UserDto;
import nl.consumergram.consumergramv2.models.Authority;
import nl.consumergram.consumergramv2.models.User;
import nl.consumergram.consumergramv2.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

import static nl.consumergram.consumergramv2.services.UserService.fromUser;

@Service
public class AuthorityService {

    UserRepository userRepository;

    public AuthorityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //    Haalt de rollen (authorities) van een gebruiker op basis van de gebruikersnaam. Gooit een
    public Set<Authority> getAuthorities(String username) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        UserDto userDto = fromUser(user);
        return userDto.getAuthorities();
    }

    //    Voegt een nieuwe autoriteit (rol) toe aan een gebruiker.
    public void addAuthority(String username, String authority) {

        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        user.addAuthority(new Authority(username, authority));
        userRepository.save(user);
    }

    //    Verwijdert een autoriteit van een gebruiker op basis van de gebruikersnaam en autoriteit.
    public void removeAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authority)).findAny().get();
        user.removeAuthority(authorityToRemove);
        userRepository.save(user);
    }

}
