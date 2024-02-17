package nl.consumergram.consumergramv2.services;

import lombok.Data;
import nl.consumergram.consumergramv2.models.ImageData;
import nl.consumergram.consumergramv2.models.User;
import nl.consumergram.consumergramv2.repositories.ImageDataRepository;
import nl.consumergram.consumergramv2.repositories.UserRepository;
import nl.consumergram.consumergramv2.utils.ImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Data
@Service
public class ImageDataService {

    //    (imageDataRepository en userRepository) worden geïnjecteerd via de constructor.
    private final ImageDataRepository imageDataRepository;
    private final UserRepository userRepository;

    public ImageDataService(ImageDataRepository imageDataRepository, UserRepository userRepository) {
        this.imageDataRepository = imageDataRepository;
        this.userRepository = userRepository;
    }

    public String uploadImage(MultipartFile multipartFile, String username) throws IOException {
        //        Dit in een if statement zetten?
        Optional<User> user = userRepository.findById(username);
        User user1 = user.get();

//        Maakt een nieuwe image data aan en zet de naam, type en imageData
//        van de image data naar de gegevens van de multipart file

        ImageData imgData = new ImageData();
        imgData.setName(multipartFile.getName());
        imgData.setType(multipartFile.getContentType());
        imgData.setImageData(ImageUtil.compressImage(multipartFile.getBytes()));
        imgData.setUser(user1);

        ImageData savedImage = imageDataRepository.save(imgData);
        user1.setImageData(imgData);
        userRepository.save(user1);
        return savedImage.getName();
    }






    public byte[] downloadImage(String username) throws IOException {
        Optional<User> userOptional = userRepository.findById(username);

        if (userOptional.isPresent()) {
            User user1 = userOptional.get();
            ImageData imageData = user1.getImageData();

            if (imageData != null) {
                return ImageUtil.decompressImage(imageData.getImageData());
            } else {
                // Gebruiker heeft geen afbeeldingsgegevens
                return "Gebruiker heeft geen afbeeldingsgegevens".getBytes();
            }
        } else {
            // Gebruiker niet gevonden
            return "Gebruiker niet gevonden".getBytes();
        }
    }

    public void deleteImage(User user) {
        if (user.getImageData() != null) {
            // Verwijder de afbeeldingsgegevens uit de database
            imageDataRepository.delete(user.getImageData());
            // Zet de referentie naar de afbeelding van de gebruiker op null
            user.setImageData(null);
            userRepository.save(user);
        }


    }

}
