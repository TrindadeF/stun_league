package stun.league.com.StunLeague.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import stun.league.com.StunLeague.domain.models.dto.player.PlayerResponseDTO;

import java.util.Set;


@Data
@AllArgsConstructor
public class TeamsMatch {


    private Set<PlayerResponseDTO> BL;
    private Set<PlayerResponseDTO> GR;

    public TeamsMatch() {

    }

}
