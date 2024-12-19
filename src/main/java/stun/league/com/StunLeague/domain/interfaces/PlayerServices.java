package stun.league.com.StunLeague.domain.interfaces;

import org.springframework.data.domain.Page;
import stun.league.com.StunLeague.domain.enums.PlayerStatus;
import stun.league.com.StunLeague.domain.models.dto.player.MatchPlayerResponseDTO;
import stun.league.com.StunLeague.domain.models.dto.player.PlayerResponseDTO;
import stun.league.com.StunLeague.infra.entities.Player;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface PlayerServices {

    PlayerResponseDTO findByUsername(String username);

    void setPointsToPlayersWinners(Set<PlayerResponseDTO> teamPlayers, Date date);

    void setPointsToPlayersLossers(Set<PlayerResponseDTO> teamPlayers, Date date);

    Boolean isStatus(Long playerId, PlayerStatus playerStatus);

    void setStatusPlayer(Long playerId, PlayerStatus status);
    void setStatusPlayer(String playerName, PlayerStatus status);

    List<MatchPlayerResponseDTO> getMatchesPlayer(Long playerId);

    Page<PlayerResponseDTO> getPlayers(int pageNo, int pageSize);
}
