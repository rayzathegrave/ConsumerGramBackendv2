package nl.consumergram.consumergramv2.config;

import nl.consumergram.consumergramv2.filter.JwtRequestFilter;
import nl.consumergram.consumergramv2.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*  Deze security is niet de enige manier om het te doen.
    In de andere branch van deze github repo staat een ander voorbeeld
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    public final CustomUserDetailsService customUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // PasswordEncoderBean. Deze kun je overal in je applicatie injecteren waar nodig.
    // Je kunt dit ook in een aparte configuratie klasse zetten.
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authenticatie met customUserDetailsService en passwordEncoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        var auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(passwordEncoder);
        auth.setUserDetailsService(customUserDetailsService);
        return new ProviderManager(auth);
    }

    // Authorizatie met jwt
    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth ->
                                auth
                                        // Wanneer je deze uncomment, staat je hele security open. Je hebt dan alleen nog een jwt nodig.
//                .requestMatchers("/**").permitAll()

                                        //AUTHENTICATED
                                        .requestMatchers(HttpMethod.GET, "/authenticated").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/authenticate").permitAll()
//BLOGPOSTS
                                        .requestMatchers(HttpMethod.GET, "/blog-posts/{username}/{id}").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/blog-posts/{username}").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/blog-posts").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/blog-posts/{username}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/blog-posts/{username}/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//IMAGE
                                        .requestMatchers(HttpMethod.POST, "/image").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.GET, "/image/{username}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/image/{usename}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//USERS
                                        .requestMatchers(HttpMethod.GET, "/users").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.GET, "/users/{username}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/users/admin").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "/users/{username}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/users/{username}").hasAuthority("ROLE_ADMIN")

                                        .requestMatchers(HttpMethod.GET, "/users/{username}/authorities").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/users/{username}/authorities").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/users/{username}/authorities/{authority}").hasAuthority("ROLE_ADMIN")
//USERPROFILE
                                        .requestMatchers(HttpMethod.GET, "/user-profile").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/user-profile/{username}").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/user-profile/{username}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

//
//                                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}