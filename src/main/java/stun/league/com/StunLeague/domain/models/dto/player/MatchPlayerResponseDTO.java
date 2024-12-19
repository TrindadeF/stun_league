package stun.league.com.StunLeague.domain.models.dto.player;

import java.time.LocalDate;

public record MatchPlayerResponseDTO(LocalDate matchDate, Integer wins, Integer losses) {
}
