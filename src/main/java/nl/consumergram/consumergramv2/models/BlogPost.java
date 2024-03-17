package nl.consumergram.consumergramv2.models;

import jakarta.persistence.*;


import lombok.Data;
import nl.consumergram.consumergramv2.utils.Category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Entity
@Table(name = "blog_posts")
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String price;

    @OneToMany(mappedBy = "blogPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Upvote> upvotesList = new ArrayList<>();

    private int upvotes = 0;

    public void incrementUpvotes() {
        this.upvotes++;
    }

    @Lob
    private byte[] imageData;

    @Column(name = "yes_no_option")
    private boolean yesNoOption;

    @Column(columnDefinition = "TEXT")
    private String caption;
//    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
//     Username is uniek in de user omdat we maar 1 username willen opslaan maar dat kan in andere gevallen ook een id of een product name zijn etc.
//     @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JoinColumn(name = "user_username", referencedColumnName = "username")

    private User user;


    @ElementCollection(targetClass = Category.class)
    @CollectionTable(name = "blog_post_categories", joinColumns = @JoinColumn(name = "blog_post_id"))
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Set<Category> categories = new HashSet<>();


}
