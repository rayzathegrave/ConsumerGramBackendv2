package nl.consumergram.consumergramv2.dtos;

import lombok.Data;
import nl.consumergram.consumergramv2.utils.Category;
import org.springframework.web.multipart.MultipartFile;

import java.nio.channels.MulticastChannel;
import java.util.Set;

@Data
public class InputBlogpostDto {
    private Long id;

    private String caption;
    private String username;
    private MultipartFile file;
    private String price;
    private Set<Category> categories;
    private boolean yesNoOption;

}