package nl.consumergram.consumergramv2.dtos;


import lombok.Data;

@Data
public class OutputUpvoteDto {
    private Long id;
    private String username;
    private Long blogPostId;

    // getters and setters
}