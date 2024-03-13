package nl.consumergram.consumergramv2.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Upvote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private BlogPost blogPost;

}