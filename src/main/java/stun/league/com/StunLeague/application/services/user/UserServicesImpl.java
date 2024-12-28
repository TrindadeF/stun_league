package stun.league.com.StunLeague.application.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stun.league.com.StunLeague.domain.enums.PlayerStatus;
import stun.league.com.StunLeague.domain.exceptions.FailedToSaveUserException;
import stun.league.com.StunLeague.domain.exceptions.FailedToUploadImageProfileException;
import stun.league.com.StunLeague.domain.exceptions.NotFoundException;
import stun.league.com.StunLeague.domain.exceptions.NotFoundUserException;
import stun.league.com.StunLeague.domain.interfaces.UserServices;
import stun.league.com.StunLeague.domain.interfaces.facade.UserUpdateInformationFacade;
import stun.league.com.StunLeague.domain.models.dto.configuration.RequestUpdateConfigurationDTO;
import stun.league.com.StunLeague.domain.models.dto.configuration.UserConfigurationDTO;
import stun.league.com.StunLeague.domain.models.dto.socialMedia.RequestUpdateSocialMediaDTO;
import stun.league.com.StunLeague.domain.models.dto.socialMedia.UserSocialMediaDTO;
import stun.league.com.StunLeague.domain.models.dto.user.*;
import stun.league.com.StunLeague.domain.models.dto.validations.ValidationResultDTO;
import stun.league.com.StunLeague.infra.data.repositories.ConfigurationRepository;
import stun.league.com.StunLeague.infra.data.repositories.PlayerRepository;
import stun.league.com.StunLeague.infra.data.repositories.SocialMediaRepository;
import stun.league.com.StunLeague.infra.data.repositories.UserRepository;
import stun.league.com.StunLeague.infra.entities.Player;
import stun.league.com.StunLeague.infra.entities.User;
import stun.league.com.StunLeague.infra.entities.UserConfiguration;
import stun.league.com.StunLeague.infra.entities.UserSocialMedia;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class UserServicesImpl  implements UserServices {

    private final UserRepository userRepository;
    private final SocialMediaRepository socialMediaRepository;
    private final ConfigurationRepository configurationRepository;
    private final UserUpdateInformationFacade userUpdateInformationFacade;
    private final PlayerRepository playerRepository;
    private static final String UPLOAD_DIRECTORY = "uploads/";

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServicesImpl(UserRepository userRepository, SocialMediaRepository socialMediaRepository, ConfigurationRepository configurationRepository, UserUpdateInformationFacade userUpdateInformationFacade, PlayerRepository playerRepository) {
        this.userRepository = userRepository;
        this.socialMediaRepository = socialMediaRepository;
        this.configurationRepository = configurationRepository;
        this.userUpdateInformationFacade = userUpdateInformationFacade;
        this.playerRepository = playerRepository;
    }

    @Override
    public UserSavedResponseDTO saveUser(UserRequestSaveDTO dto) {
        User user = dto.toEntity();
        Player player = Player.builder()
                .id(null)                          // ID pode ser nulo porque é gerado automaticamente
                .username(dto.username())          // Nome do jogador
                .wins(0)                           // Número inicial de vitórias
                .losses(0)                         // Número inicial de derrotas
                .points(BigDecimal.ZERO)           // Pontos iniciais
                .user(user)                        // Objeto User relacionado
                .playerStatus(PlayerStatus.DEFAULT) // Status inicial do jogador
                .matches(new ArrayList<>())        // Lista vazia de partidas
                .build();

        player.setUser(user);
        user.setPlayer(player);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setImageProfile("uploads\\image-profile-users\\default\\icon.png");
        try {
            user = this.userRepository.save(user);
            return this.userToDto(user);

        } catch (Exception e) {
            throw new FailedToSaveUserException(e.getMessage());
        }
    }

    @Override
    public UserSavedResponseDTO findById(Long id) {
        return this.userToDto(this.userRepository.findById(id).orElseThrow(() -> new NotFoundUserException("Usuario não encontrado!")));

    }

    @Override
    public UserSavedResponseDTO findByUsername(String name) {
        return this.userToDto(this.userRepository.findByName(name).orElseThrow(() -> new NotFoundUserException("Usuario não encontrado!")));

    }

    @Override
    public UserResponseDTO findByIdInformations(Long id) {
        var user = this.userRepository.findById(id).orElseThrow(() -> new NotFoundUserException("Usuario não encontrado!"));
        return this.userToDtoInformations(user, user.getPlayer());
    }

    @Override
    public UserResponseDTO findByUsernameInformations(String name) {
        var user = this.userRepository.findByName(name).orElseThrow(() -> new NotFoundUserException("Usuario não encontrado!"));
        return this.userToDtoInformations(user, user.getPlayer());
    }

    @Override
    public UserResponseDTO updateSocialMedia(RequestUpdateSocialMediaDTO requestUpdateSocialMediaDTO) {
        var user = this.userRepository.findById(requestUpdateSocialMediaDTO.userId()).orElseThrow(() -> new NotFoundUserException("Usuario não encontrado!"));
        var socialMedia = this.socialMediaRepository.findById(requestUpdateSocialMediaDTO.socialMediaId()).orElseThrow(() -> new NotFoundException("Social media não encontrada!"));
        UserSocialMedia userSocialMedia = user.getUserSocialMedia().stream()
                .filter(x -> Objects.equals(x.getUser().getId(), requestUpdateSocialMediaDTO.userId()) &&
                        Objects.equals(x.getSocialMedia().getId(), requestUpdateSocialMediaDTO.socialMediaId()))
                .findFirst()
                .orElse(null);

        if (userSocialMedia != null) {
            userSocialMedia.setValue(requestUpdateSocialMediaDTO.value());
        } else {
            userSocialMedia = new UserSocialMedia(null, user, socialMedia, requestUpdateSocialMediaDTO.value());
            user.getUserSocialMedia().add(userSocialMedia);
        }
        this.userRepository.save(user);
        return this.userToDtoInformations(user, user.getPlayer());
    }

    @Override
    public UserResponseDTO updateConfiguration(RequestUpdateConfigurationDTO requestUpdateConfigurationDTO) {
        var user = this.userRepository.findById(requestUpdateConfigurationDTO.userId()).orElseThrow(() -> new NotFoundUserException("Usuario não encontrado!"));
        var configuration = this.configurationRepository.findById(requestUpdateConfigurationDTO.configurationId()).orElseThrow(() -> new NotFoundException("Social media não encontrada!"));
        UserConfiguration userConfiguration = user.getUserConfigurations().stream()
                .filter(x -> Objects.equals(x.getUser().getId(), requestUpdateConfigurationDTO.userId()) &&
                        Objects.equals(x.getConfiguration().getId(), requestUpdateConfigurationDTO.configurationId()))
                .findFirst()
                .orElse(null);

        if (userConfiguration != null) {
            userConfiguration.setValue(requestUpdateConfigurationDTO.value());
        } else {
            userConfiguration = new UserConfiguration(null, user, configuration, requestUpdateConfigurationDTO.value());
            user.getUserConfigurations().add(userConfiguration);
        }
        this.userRepository.save(user);
        return this.userToDtoInformations(user, user.getPlayer());
    }

    @Override
    public UserResponseDTO updateUserInformations(RequestUpdateUserInformationDTO requestUpdateUserInformationDTO) {
        var user = this.userRepository.findById(requestUpdateUserInformationDTO.userId()).orElseThrow(() -> new NotFoundUserException("Usuario não encontrado!"));

        this.userUpdateInformationFacade.validate(requestUpdateUserInformationDTO);

        user.setName(requestUpdateUserInformationDTO.name() != null && !requestUpdateUserInformationDTO.name().isBlank()
                ? requestUpdateUserInformationDTO.name()
                : user.getName());

        user.getPlayer().setUsername(requestUpdateUserInformationDTO.username() != null && !requestUpdateUserInformationDTO.username().isBlank()
                ? requestUpdateUserInformationDTO.username()
                : user.getPlayer().getUsername());

        user.setEmail(requestUpdateUserInformationDTO.email() != null && !requestUpdateUserInformationDTO.email().isBlank()
                ? requestUpdateUserInformationDTO.email()
                : user.getEmail());

        user.setBirthDate(requestUpdateUserInformationDTO.birthDate());

//        user.setDescription(requestUpdateUserInformationDTO.description() != null && !requestUpdateUserInformationDTO.description().isBlank()
//                ? requestUpdateUserInformationDTO.description()
//                : user.getDescription());
        this.userRepository.save(user);
        return this.userToDtoInformations(user, user.getPlayer());
    }

    @Override
    public void uploadImageProfile(Long userId, MultipartFile file) {
        try {
            var user = this.userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("Usuario não encontrado"));
            Path uploadPath = Paths.get(UPLOAD_DIRECTORY + "/image-profile-users/" + user.getName() + "-" + user.getId());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String newFilename = user.getName() + "-" + user.getId() + "." + fileExtension;

            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            user.setImageProfile(filePath.toString());
            this.userRepository.save(user);

        } catch (IOException e) {
            throw new FailedToUploadImageProfileException("Ocorreu ao fazer upload da imagem.");
        }
    }


    @Override
    public DownloadImageProfileResponseDTO downloadImageProfile(Long userId) {
        try {
            var user = this.userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("Usuario não encontrado"));
            String filePathString = user.getImageProfile();
            if (filePathString != null) {

                Path filePath = Paths.get(filePathString);

                if (Files.exists(filePath)) {
                    // Lê o conteúdo do arquivo
                    byte[] fileContent = Files.readAllBytes(filePath);

                    MediaType mediaType = getMediaType(filePath.toString());
                    return new DownloadImageProfileResponseDTO(mediaType, fileContent);
                }
            }

        } catch (IOException e) {
            return null;
        }
        return null;

    }

    @Override
    public List<ValidationResultDTO> validate(UserRequestSaveDTO userDTO) {
        List<ValidationResultDTO> validations = new ArrayList<>();
        if (this.userRepository.existByEmail(userDTO.email())) {
            validations.add(new ValidationResultDTO("email", "Email já existe"));
        }

        if (this.playerRepository.existByUsername(userDTO.username())) {
            validations.add(new ValidationResultDTO("username", "Username já existe"));
        }

        if ( (LocalDate.now().getYear() - userDTO.birthDate().getYear())  < 15) {
            validations.add(new ValidationResultDTO("birthDate", "Usuario deve ter mais de 15 anos"));
        }

        if (this.userRepository.existsByCpf(userDTO.cpf())) {
            validations.add(new ValidationResultDTO("cpf", "Cpf já existe"));
        }

        if (userDTO.password().length() < 6) {
            validations.add(new ValidationResultDTO("password", "Senha deve ter mais 6 ou mais caracteres"));

        }
        return validations;
    }


    private UserSavedResponseDTO userToDto( User user) {
        return new UserSavedResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPlayer().getId());

    }

    private UserResponseDTO userToDtoInformations(User user, Player player) {
        return new UserResponseDTO(player.getUsername(), user.getName(), user.getEmail(), user.getBirthDate(),
                this.toUserSocialDTOList(player.getUser().getUserSocialMedia()),
                this.userConfigurationDTOList(player.getUser().getUserConfigurations()));

    }

    private List<UserSocialMediaDTO> toUserSocialDTOList(List<UserSocialMedia> userSocialMedia) {

        return userSocialMedia.stream().map(this::toUserSocialMediaDTO).toList();
    }

    private List<UserConfigurationDTO> userConfigurationDTOList(List<UserConfiguration> userConfigurations) {

        return userConfigurations.stream().map(this::toUserConfigurationDTO).toList();
    }

    private UserSocialMediaDTO toUserSocialMediaDTO(UserSocialMedia userSocialMedia) {
        return new UserSocialMediaDTO(userSocialMedia.getId(), userSocialMedia.getUser().getId(), userSocialMedia.getSocialMedia().getId(), userSocialMedia.getValue());
    }

    private UserConfigurationDTO toUserConfigurationDTO(UserConfiguration userConfiguration) {
        return new UserConfigurationDTO(userConfiguration.getId(), userConfiguration.getUser().getId(), userConfiguration.getConfiguration().getId(), userConfiguration.getValue());
    }
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private MediaType getMediaType(String fileName) {
        if (fileName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM; // Default
        }
    }

}
