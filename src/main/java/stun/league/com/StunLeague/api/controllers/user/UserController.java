package stun.league.com.StunLeague.api.controllers.user;


import jakarta.validation.Valid;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stun.league.com.StunLeague.domain.interfaces.UserServices;
import stun.league.com.StunLeague.domain.models.dto.configuration.RequestUpdateConfigurationDTO;
import stun.league.com.StunLeague.domain.models.dto.socialMedia.RequestUpdateSocialMediaDTO;
import stun.league.com.StunLeague.domain.models.dto.user.RequestUpdateUserInformationDTO;
import stun.league.com.StunLeague.domain.models.dto.user.UserRequestSaveDTO;
import stun.league.com.StunLeague.domain.models.dto.user.UserResponseDTO;
import stun.league.com.StunLeague.domain.models.dto.user.UserSavedResponseDTO;

import java.util.HashMap;

@Controller
@RequestMapping("/v1/users")

//@CrossOrigin(origins = "http://localhost:4200")
// Definindo o scopo dessa instancia sendo por request (Para cada nova request, essa classe é instanciada!)
@Scope("request")

public class UserController {

    private final UserServices userServices;

    private HashMap<String, String> errosValidation = new HashMap<>();


    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("/register")
    public ResponseEntity saveUser(@Valid @RequestBody UserRequestSaveDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            this.errosValidation.put(bindingResult.getFieldError().getField(), bindingResult.getFieldError().getDefaultMessage());
            System.out.println(this.errosValidation.keySet());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errosValidation);
        }
        var validations = this.userServices.validate(userDTO);
        if (!validations.isEmpty()) {
            for (var validation : validations) {
                this.errosValidation.put(validation.propertyName(), validation.value());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errosValidation);
        }

        return ResponseEntity.ok(this.userServices.saveUser(userDTO));
    }


    @GetMapping("{id}")
    public ResponseEntity<UserSavedResponseDTO> getUserById(@PathVariable Long id) {
        var user = this.userServices.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("informations/{id}")
    public ResponseEntity<UserResponseDTO> getUserByIdInformations(@PathVariable Long id) {
        var user = this.userServices.findByIdInformations(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("update-social-media")
    public ResponseEntity<UserResponseDTO> updateSocialMedia(@RequestBody RequestUpdateSocialMediaDTO requestUpdateSocialMediaDTO) {
        var user = this.userServices.updateSocialMedia(requestUpdateSocialMediaDTO);
        return ResponseEntity.ok(user);

    }

    @PostMapping("update-configuration")
    public ResponseEntity<UserResponseDTO> updateConfiguration(@RequestBody RequestUpdateConfigurationDTO requestUpdateConfigurationDTO) {
        var user = this.userServices.updateConfiguration(requestUpdateConfigurationDTO);
        return ResponseEntity.ok(user);

    }
    @PostMapping("update-user-informations")
    public ResponseEntity updateUserInformations(@RequestBody RequestUpdateUserInformationDTO requestUpdateUserInformationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            this.errosValidation.put(bindingResult.getFieldError().getField(), bindingResult.getFieldError().getDefaultMessage());
            System.out.println(this.errosValidation.keySet());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errosValidation);
        }
        var user = this.userServices.updateUserInformations(requestUpdateUserInformationDTO);
        return ResponseEntity.ok(user);
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<UserSavedResponseDTO> getUserByUsername(@PathVariable String username) {
        var user = this.userServices.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/upload-profile-image")
    public ResponseEntity uploadProfileImage(
            @RequestParam("userId") Long userId,
            @RequestParam("file") MultipartFile file) {

        this.userServices.uploadImageProfile(userId, file);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/download-image-profile/{userId}")
    public ResponseEntity<byte[]> downloadImageProfile(@PathVariable Long userId) {
        var imageProfile = this.userServices.downloadImageProfile(userId);
        if (imageProfile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(imageProfile.mediaType())
                .body(imageProfile.fileContent());
}


}
