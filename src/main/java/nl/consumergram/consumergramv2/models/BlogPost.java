package nl.consumergram.consumergramv2.models;

import jakarta.persistence.*;


import lombok.Data;

@Data
@Entity
@Table(name = "blog_posts")
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;

    @Lob
    private byte[] imageData;



    @Column(columnDefinition = "TEXT")
    private String caption;
//    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
//     Username is uniek in de user omdat we maar 1 username willen opslaan maar dat kan in andere gevallen ook een id of een product name zijn etc.
//     @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JoinColumn(name = "user_username", referencedColumnName = "username")

    private User user;




}
