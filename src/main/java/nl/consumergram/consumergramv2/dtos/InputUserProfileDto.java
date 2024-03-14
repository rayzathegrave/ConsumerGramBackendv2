package nl.consumergram.consumergramv2.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class InputUserProfileDto {
    private Long id;

    private String email;

    @Size(max=50,message="criteria not met")
    private String name;

    private String regio;
    private String bio;
    private MultipartFile file;
    private String username;
}
