package nl.consumergram.consumergramv2.dtos;

import lombok.Data;

@Data
public class OutputUserProfileDto {
    private Long id;

    private String email;
    private String name;
    private String regio;
    private String bio;
    private byte[] fileContent;
    private String username;
}