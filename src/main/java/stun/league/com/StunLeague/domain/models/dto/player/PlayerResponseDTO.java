package stun.league.com.StunLeague.domain.models.dto.player;

import java.math.BigDecimal;

public record PlayerResponseDTO(Long id, String username, Integer wins, Integer losses, BigDecimal points, Long userId) {
}



