package nl.consumergram.consumergramv2.dtos;

import lombok.Data;
import nl.consumergram.consumergramv2.models.ImageData;
import org.springframework.web.multipart.MultipartFile;

@Data
public class OutputBlogpostDto {
    private Long id;
    private String caption;
    private String username;
    private byte[] fileContent; // replace MultipartFile with byte[]
}
