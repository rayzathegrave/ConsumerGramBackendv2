package nl.consumergram.consumergramv2.dtos;

import lombok.Data;
import nl.consumergram.consumergramv2.models.ImageData;
import nl.consumergram.consumergramv2.utils.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class OutputBlogpostDto {
    private Long id;
    private String caption;
    private String username;
    private byte[] fileContent; // replace MultipartFile with byte[]
    private String price;
    private Set<Category> categories;
}
