package stun.league.com.StunLeague.domain.models.dto.user;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RequestUpdateUserInformationDTO(


        Long userId,

        String username,

        String name,

        String email,

        LocalDate birthDate,

        String description) {
}