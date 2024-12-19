package stun.league.com.StunLeague.application.mappers;

import stun.league.com.StunLeague.domain.models.dto.player.MatchPlayerResponseDTO;
import stun.league.com.StunLeague.infra.entities.MatchPlayer;
import stun.league.com.StunLeague.infra.entities.Player;
import stun.league.com.StunLeague.domain.models.dto.player.PlayerResponseDTO;

public class PlayerMappers {


    public static PlayerResponseDTO toPlayerResponseDTO(Player player) {

        return new PlayerResponseDTO(player.getId(), player.getUsername(), player.getWins(), player.getLosses(), player.getPoints(), player.getUser().getId());
    }

    public static MatchPlayerResponseDTO toMatchPlayerDTO(MatchPlayer matchPlayer) {
        return new MatchPlayerResponseDTO(matchPlayer.getMatchDate(), matchPlayer.getWins(), matchPlayer.getLosses());
    }
}
