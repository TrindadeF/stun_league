package stun.league.com.StunLeague.api.controllers.points;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import stun.league.com.StunLeague.domain.models.dto.points.PointResultDTO;
import stun.league.com.StunLeague.infra.data.repositories.PlayerRepository;

@Controller
@RequestMapping("/v1/points")
@CrossOrigin("*")
public class StunPointsController {

    private final PlayerRepository playerRepository;

    public StunPointsController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping("{username}")
    public ResponseEntity<PointResultDTO> getPointsByUsername(@PathVariable String username) {
        var player = this.playerRepository.findByUsername(username);
        return ResponseEntity.ok(new PointResultDTO(player.get().getUsername(), player.get().getPoints()));
    }
}
