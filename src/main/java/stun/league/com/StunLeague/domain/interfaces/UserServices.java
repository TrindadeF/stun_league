package stun.league.com.StunLeague.domain.interfaces;

import org.springframework.web.multipart.MultipartFile;
import stun.league.com.StunLeague.domain.models.dto.configuration.RequestUpdateConfigurationDTO;
import stun.league.com.StunLeague.domain.models.dto.socialMedia.RequestUpdateSocialMediaDTO;
import stun.league.com.StunLeague.domain.models.dto.user.*;
import stun.league.com.StunLeague.domain.models.dto.validations.ValidationResultDTO;

import java.util.List;

public interface UserServices {


    UserSavedResponseDTO saveUser(UserRequestSaveDTO userDTO);

    UserSavedResponseDTO findById(Long id);

    UserSavedResponseDTO findByUsername(String username);

    UserResponseDTO findByIdInformations(Long id);

    UserResponseDTO findByUsernameInformations(String username);

    UserResponseDTO updateSocialMedia(RequestUpdateSocialMediaDTO requestUpdateSocialMediaDTO);

    UserResponseDTO updateConfiguration(RequestUpdateConfigurationDTO requestUpdateConfigurationDTO);

    UserResponseDTO updateUserInformations(RequestUpdateUserInformationDTO requestUpdateUserInformationDTO);

    void uploadImageProfile(Long userId, MultipartFile file);

    DownloadImageProfileResponseDTO downloadImageProfile(Long userId);

    List<ValidationResultDTO> validate(UserRequestSaveDTO userDTO);
}
