package nl.consumergram.consumergramv2.dtos;


//UserDTO basisfunctionaliteit voor gebruikersbeheer


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import nl.consumergram.consumergramv2.models.Authority;

import java.util.Set;

public class UserDto {

    @Size(max=50,min=1,message="criteria not met")
    public String username;
    @Pattern(regexp="^(?=.*[!@#$%^&*]).{8,}$", message="criteria not met")
    public String password;
    public Boolean enabled;
    public String apikey;
    public String email;
    public Set<Authority> authorities;

//    Getter methodes om de gegevens op te halen
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getApikey() {
        return apikey;
    }

    public String getEmail() {
        return email;
    }

//    Er zijn setter-methoden voor elk veld. Deze methoden worden gebruikt om de waarden van de velden in te stellen.

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}