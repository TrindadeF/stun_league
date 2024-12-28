package stun.league.com.StunLeague.api.controllers.players;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import stun.league.com.StunLeague.domain.enums.PlayerStatus;
import stun.league.com.StunLeague.domain.interfaces.PlayerServices;
import stun.league.com.StunLeague.domain.models.dto.player.MatchPlayerResponseDTO;
import stun.league.com.StunLeague.domain.models.dto.player.PlayerResponseDTO;
import stun.league.com.StunLeague.infra.entities.Player;

import java.util.List;

@Controller
@RequestMapping("/v1/players")
public class PlayerController {

    private final PlayerServices playerServices;

    public PlayerController(PlayerServices playerServices) {
        this.playerServices = playerServices;
    }

    @GetMapping("username/{username}")
    public ResponseEntity<PlayerResponseDTO> getByUsername(@PathVariable String username ) {
        return ResponseEntity.ok(this.playerServices.findByUsername(username));
    }

// Depois simplicar isto em apenas um endpoint
    @GetMapping("in-match-screen/{playerId}")
    public ResponseEntity<Boolean> inMatch(@PathVariable Long playerId) {
        return ResponseEntity.ok(this.playerServices.isStatus(playerId, PlayerStatus.IN_MATCH_SCREEN));
    }

    @GetMapping("in-ant-cheater-screen/{playerId}")
    public ResponseEntity<Boolean> inAntCheater(@PathVariable Long playerId) {
        return ResponseEntity.ok(this.playerServices.isStatus(playerId, PlayerStatus.IN_ANT_CHEATER_SCREEN));
    }

    @GetMapping("in-queue-waiting-screen/{playerId}")
    public ResponseEntity<Boolean> inQueue(@PathVariable Long playerId) {
        return ResponseEntity.ok(this.playerServices.isStatus(playerId, PlayerStatus.IN_QUEUE_SCREEN));
    }

    @GetMapping("match-players/{playerId}")
    public ResponseEntity<List<MatchPlayerResponseDTO>> getMatchesPlayer(@PathVariable Long playerId) {
        return ResponseEntity.ok(this.playerServices.getMatchesPlayer(playerId));
    }

    @GetMapping
    public ResponseEntity<Page<PlayerResponseDTO>> getPlayers(@RequestParam(defaultValue = "0") int pageNo,
                                                   @RequestParam(defaultValue = "10") int pageSize) {
        Page<PlayerResponseDTO> players = this.playerServices.getPlayers(pageNo, pageSize);
        return ResponseEntity.ok(players);

    }
    @GetMapping("/top10")
    public List<PlayerResponseDTO> getTop10Players() {
        return playerServices.getTop10Players();
    }
}
