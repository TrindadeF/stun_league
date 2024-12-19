package stun.league.com.StunLeague.domain.models.dto.auth;

import java.util.Date;

public record AuthenticationResponseDTO(
        Long userId,
        Long playerId,
        String name,
        String username,
        String email,
        String imageProfile,
        String token,
        Date expiration,
        PlayerInformationsDTO playerInformationsDTO
        ) {
}
