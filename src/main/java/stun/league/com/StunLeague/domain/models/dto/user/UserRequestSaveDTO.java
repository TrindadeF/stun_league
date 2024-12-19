package stun.league.com.StunLeague.domain.models.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import stun.league.com.StunLeague.infra.entities.User;

import java.time.LocalDate;

public record UserRequestSaveDTO(

        @NotNull(message = "O nome não pode estar vazio")
        @NotBlank(message = "O nome não pode estar vazio")
        @Size(min= 5, max = 50, message = "O nome deve ter entre 5 a 50 caracteres")
        String name,
        @NotNull(message = "O username não pode estar vazio")
        @NotBlank(message = "O username não pode estar vazio")
        @Size(min = 5, max = 10, message = "O username deve ter entre 5 a 10 caracteres")
        String username,
        @NotNull(message = "O Email não pode estar vazio")
        @NotBlank(message = "O Email não pode estar vazio")
        @Email(message = "Formato incorreto!")
        String email,
        @NotNull(message = "O Cpf não pode estar vazio")
        @NotBlank(message = "O Cpf não pode estar vazio")
        String cpf,
        @NotNull(message = "Data de nascimento não pode estar vazia")
        LocalDate birthDate,
        @NotBlank(message = "Sua senha não pode estar vazia")
        @NotNull(message = "Sua senha não pode estar vazia!")
        String password) {


    public User toEntity() {
        return new User(null, name(), email(), cpf(), birthDate(), password(), null, null, null, null);
    }
}
