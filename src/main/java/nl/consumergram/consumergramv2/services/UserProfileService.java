package nl.consumergram.consumergramv2.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import nl.consumergram.consumergramv2.dtos.InputUserProfileDto;
import nl.consumergram.consumergramv2.dtos.OutputUserProfileDto;
import nl.consumergram.consumergramv2.models.User;
import nl.consumergram.consumergramv2.models.UserProfile;
import nl.consumergram.consumergramv2.repositories.UserProfileRepository;
import nl.consumergram.consumergramv2.utils.ImageUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }


    @Transactional
    public List<OutputUserProfileDto> getAlleUserProfiles() {
        List<UserProfile> userProfileList = userProfileRepository.findAll();

        List<OutputUserProfileDto> outputUserProfileDtoList = new ArrayList<>();

        for (UserProfile userProfile : userProfileList) {
            OutputUserProfileDto outputUserProfileDto = new OutputUserProfileDto();

            outputUserProfileDto.setEmail(userProfile.getEmail());
            outputUserProfileDto.setName(userProfile.getName());
            outputUserProfileDto.setRegio(userProfile.getRegio());
            outputUserProfileDto.setBio(userProfile.getBio());
            outputUserProfileDto.setUsername(userProfile.getUser().getUsername());
            outputUserProfileDto.setId(userProfile.getId());
            outputUserProfileDto.setFileContent(ImageUtil.decompressImage(userProfile.getImageData()));
            outputUserProfileDtoList.add(outputUserProfileDto);
        }
        return outputUserProfileDtoList;
    }









    public OutputUserProfileDto createUserProfile(InputUserProfileDto inputUserProfileDto) throws IOException {
        UserProfile userProfile = new UserProfile();

        // Set values from DTO to UserProfile object
        userProfile.setEmail(inputUserProfileDto.getEmail());
        userProfile.setName(inputUserProfileDto.getName());
        userProfile.setRegio(inputUserProfileDto.getRegio());
        userProfile.setBio(inputUserProfileDto.getBio());
        userProfile.setImageData(ImageUtil.compressImage(inputUserProfileDto.getFile().getBytes()));


        // Assuming username is provided in the DTO
        if (inputUserProfileDto.getUsername() != null) {
            // Create a User object and set its username
            User user = new User();
            user.setUsername(inputUserProfileDto.getUsername());

            // Set the User object to the UserProfile
            userProfile.setUser(user);
        }

        // Save the UserProfile object to the repository
        userProfileRepository.save(userProfile);

        // Create an OutputUserProfileDto and set its values from the saved UserProfile object
        OutputUserProfileDto outputUserProfileDto = new OutputUserProfileDto();
        outputUserProfileDto.setEmail(userProfile.getEmail());
        outputUserProfileDto.setName(userProfile.getName());
        outputUserProfileDto.setRegio(userProfile.getRegio());
        outputUserProfileDto.setBio(userProfile.getBio());
        outputUserProfileDto.setId(userProfile.getId());
        outputUserProfileDto.setUsername(userProfile.getUser().getUsername());

        outputUserProfileDto.setFileContent(ImageUtil.decompressImage(userProfile.getImageData()));

        return outputUserProfileDto;
    }




    @Transactional
    public List<OutputUserProfileDto> getUserProfileByUsername(String username) {
        List<UserProfile> userProfileList = userProfileRepository.findByUser_Username(username)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found with username " + username));

        List<OutputUserProfileDto> outputUserProfileDtoList = new ArrayList<>();

        for (UserProfile userProfile : userProfileList) {
            OutputUserProfileDto outputUserProfileDto = new OutputUserProfileDto();

            outputUserProfileDto.setEmail(userProfile.getEmail());
            outputUserProfileDto.setName(userProfile.getName());
            outputUserProfileDto.setRegio(userProfile.getRegio());
            outputUserProfileDto.setBio(userProfile.getBio());
            outputUserProfileDto.setUsername(userProfile.getUser().getUsername());
            outputUserProfileDto.setId(userProfile.getId());
            outputUserProfileDto.setFileContent(ImageUtil.decompressImage(userProfile.getImageData()));
            outputUserProfileDtoList.add(outputUserProfileDto);
        }
        ;
        return  outputUserProfileDtoList;
    }
}