package stun.league.com.StunLeague.application.facade;

import org.springframework.stereotype.Component;
import stun.league.com.StunLeague.domain.exceptions.FailedToUpdateUserException;
import stun.league.com.StunLeague.domain.interfaces.facade.UserUpdateInformationFacade;
import stun.league.com.StunLeague.domain.models.dto.user.RequestUpdateUserInformationDTO;
import stun.league.com.StunLeague.infra.data.repositories.PlayerRepository;
import stun.league.com.StunLeague.infra.data.repositories.UserRepository;

import java.time.LocalDate;
import java.time.Period;

@Component
public class UserUpdateInformationFacadeImpl  implements UserUpdateInformationFacade {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;

    public UserUpdateInformationFacadeImpl(UserRepository userRepository, PlayerRepository playerRepository) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public void validate(RequestUpdateUserInformationDTO requestUpdateUserInformationDTO) {
        validateUsername(requestUpdateUserInformationDTO.username());
        validateName(requestUpdateUserInformationDTO.name());
        validateEmail(requestUpdateUserInformationDTO.email());
        validateBirthDate(requestUpdateUserInformationDTO.birthDate());
    }

    private void validateUsername(String username) {

        if (username != null) {
            if (this.playerRepository.existByUsername(username)) {
                throw new FailedToUpdateUserException("Username já existe.");
            }
        }

    }

    private void validateName(String name) {

        if (name != null) {
            if (name.length() < 5 || name.length() > 50) {
                throw new FailedToUpdateUserException("Nome completo inválido");
            }

            if (!name.matches("^[\\p{L} .'-]+$")) {
                throw new FailedToUpdateUserException("Nome completo inválido.");
            }

        }
    }

    private void validateEmail(String email) {

        if (email != null) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new FailedToUpdateUserException("Email inválido.");
            }
        }
    }

    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate != null) {
            if (Period.between(birthDate, LocalDate.now()).getYears() < 15) {
                throw new FailedToUpdateUserException("Usuário deve ter pelo menos 15 anos.");
            }
        }
    }

}
