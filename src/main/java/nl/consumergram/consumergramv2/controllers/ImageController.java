package nl.consumergram.consumergramv2.controllers;

import nl.consumergram.consumergramv2.models.ImageData;
import nl.consumergram.consumergramv2.models.User;
import nl.consumergram.consumergramv2.repositories.ImageDataRepository;
import nl.consumergram.consumergramv2.repositories.UserRepository;
import nl.consumergram.consumergramv2.services.ImageDataService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/image")
public class ImageController {
    private final ImageDataService imageDataService;
    private final ImageDataRepository imageDataRepository;
    private final UserRepository userRepository;
    public ImageController(ImageDataService imageDataService, ImageDataRepository imageDataRepository, UserRepository userRepository) {
        this.imageDataService = imageDataService;
        this.imageDataRepository = imageDataRepository;
        this.userRepository = userRepository;
    }
    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile multipartFile, @RequestParam String username) throws IOException {
        String image = imageDataService.uploadImage(multipartFile, username);
//        image is de naam van de file die je terug krijgt
        return ResponseEntity.ok("file had been uploaded, " + image);
    }


    //    @GetMapping("/{username}")
//    public ResponseEntity<Object> downloadImage(@PathVariable("username") String username) throws IOException {
//        byte[] image = imageDataService.downloadImage(username);
//        Optional<User> user = userRepository.findById(username);
//        Optional<ImageData> dbImageData = imageDataRepository.findById(user.get().getImageData().getId());
//        MediaType mediaType = MediaType.valueOf(dbImageData.get().getType());
////        Hier kan je specifieke datatypen meegeven, zoals image/jpeg/PDF etc.
////        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
//        return ResponseEntity.ok().contentType(mediaType).body(image);
//    }
    @GetMapping("/{username}")
    public ResponseEntity<Object> downloadImage (@PathVariable("username") String username) throws IOException {
        byte[] image = imageDataService.downloadImage(username);
        Optional<User> user = userRepository.findById(username);
        Optional<ImageData> dbImageData = imageDataRepository.findById(user.get().getImageData().getId());
        MediaType mediaType = MediaType.valueOf(dbImageData.get().getType());
        return  ResponseEntity.ok().contentType(mediaType).body(image);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteImage(@PathVariable String username) {
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            // Verwijder de profielafbeelding van de gebruiker als deze bestaat
            imageDataService.deleteImage(user.get());
            return ResponseEntity.ok("Profielafbeelding van gebruiker " + username + " succesvol verwijderd.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    Op deze manier werkt het op de JWT token
//    @GetMapping
//    public ResponseEntity<Object> downloadImage(UserDetails userDetails) throws IOException {
//        byte[] image = imageDataService.downloadImage(userDetails.getUsername());
//        Optional<User> user = userRepository.findById(userDetails.getUsername());
//        Optional<ImageData> dbImageData = imageDataRepository.findById(user.get().getImageData().getId());
//        MediaType mediaType = MediaType.valueOf(dbImageData.get().getType());
////        Hier kan je specifieke datatypen meegeven, zoals image/jpeg/PDF etc.
////        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
//        return ResponseEntity.ok().contentType(mediaType).body(image);
//    }

}