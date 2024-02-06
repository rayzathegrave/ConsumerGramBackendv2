package nl.consumergram.consumergramv2.dtos;

//De AuthenticationRequest DTO wordt gebruikt om gebruikersnaam en wachtwoordgegevens
// over te brengen bij het authenticeren van een gebruiker. De DTO bevat twee velden:
// een gebruikersnaam en een wachtwoord.

// Deze DTO wordt gebruikt bij het ontvangen van gegevens van een client (bijvoorbeeld een front-end applicatie)
// wanneer een gebruiker probeert in te loggen.

// Het biedt een gestructureerde manier om gebruikersreferenties over te dragen naar het authenticatiesysteem van de applicatie.


public class AuthenticationRequest {

    private String username;
    private String password;

    public AuthenticationRequest() {
    }
    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}