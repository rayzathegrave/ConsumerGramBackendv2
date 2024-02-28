package nl.consumergram.consumergramv2.config;

import nl.consumergram.consumergramv2.models.User;
import nl.consumergram.consumergramv2.models.Authority;
import nl.consumergram.consumergramv2.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseLoader {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("adminPassword"));

                Authority adminAuthority = new Authority();
                adminAuthority.setAuthority("ROLE_ADMIN");
                adminAuthority.setUsername(admin.getUsername()); // Set the username to the authority
                admin.getAuthorities().add(adminAuthority);

                userRepository.save(admin);
            }
        };
    }
}