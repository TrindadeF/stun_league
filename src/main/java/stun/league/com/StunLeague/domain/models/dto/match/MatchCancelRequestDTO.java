package stun.league.com.StunLeague.domain.models.dto.match;

import stun.league.com.StunLeague.domain.models.dto.player.PlayerResponseDTO;

public record MatchCancelRequestDTO(String matchId, PlayerResponseDTO playerResponseDTO) {
}
