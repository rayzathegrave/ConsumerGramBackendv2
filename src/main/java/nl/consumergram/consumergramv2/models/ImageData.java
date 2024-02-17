package nl.consumergram.consumergramv2.models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "image_data")
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    @Lob
    private byte[] imageData;

    @OneToOne
//     Username is uniek in de user omdat we maar 1 username willen opslaan maar dat kan in andere gevallen ook een id of een product name zijn etc.
//     @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JoinColumn(name = "user_username", referencedColumnName = "username")
    private User user;

    public ImageData() {
    }


}
