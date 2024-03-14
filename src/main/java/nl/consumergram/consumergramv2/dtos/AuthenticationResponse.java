package nl.consumergram.consumergramv2.dtos;


//Deze DTO wordt gebruikt om een JWT (JSON Web Token) als respons van een authenticatieverzoek terug te sturen naar de client

public class AuthenticationResponse {

    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

}