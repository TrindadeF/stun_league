package stun.league.com.StunLeague.domain.models.dto.user;

import stun.league.com.StunLeague.domain.models.dto.configuration.UserConfigurationDTO;
import stun.league.com.StunLeague.domain.models.dto.socialMedia.UserSocialMediaDTO;

import java.time.LocalDate;
import java.util.List;

public record UserResponseDTO(String username, String name, String email, LocalDate birthDate, List<UserSocialMediaDTO> userSocialMedias, List<UserConfigurationDTO> userConfigurations) {
}
