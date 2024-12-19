package stun.league.com.StunLeague.application.services.player;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import stun.league.com.StunLeague.application.mappers.PlayerMappers;
import stun.league.com.StunLeague.domain.enums.PlayerStatus;
import stun.league.com.StunLeague.domain.exceptions.NotFoundUserException;
import stun.league.com.StunLeague.domain.interfaces.PlayerServices;
import stun.league.com.StunLeague.domain.interfaces.services.MatchService;
import stun.league.com.StunLeague.domain.models.dto.player.MatchPlayerResponseDTO;
import stun.league.com.StunLeague.domain.models.dto.player.PlayerResponseDTO;
import stun.league.com.StunLeague.infra.data.repositories.MatchPlayerRepository;
import stun.league.com.StunLeague.infra.data.repositories.PlayerRepository;
import stun.league.com.StunLeague.infra.entities.MatchPlayer;
import stun.league.com.StunLeague.infra.entities.Player;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class PlayerServicesImpl  implements PlayerServices {

    private final PlayerRepository playerRepository;
    private final MatchService matchService;
    private final MatchPlayerRepository matchPlayerRepository;
    private Integer quantity;
    private Integer quantity2;


    public PlayerServicesImpl(PlayerRepository playerRepository, MatchService matchService, MatchPlayerRepository matchPlayerRepository) {
        this.playerRepository = playerRepository;
        this.matchService = matchService;
        this.matchPlayerRepository = matchPlayerRepository;
        this.quantity = 0;
        this.quantity2 = 0;
    }

    @Override
    public PlayerResponseDTO findByUsername(String username) {
        Player player = this.playerRepository.findByUsername(username).orElseThrow(() -> new NotFoundUserException("Player não encontrado"));
        return PlayerMappers.toPlayerResponseDTO(player);
    }

    @Override
    public void setPointsToPlayersWinners(Set<PlayerResponseDTO> teamPlayers, Date date) {
        System.out.println("QUANTIDADE DE VEZES QUE CHAMEI ESSE MÉTODO : " + this.quantity);
        for (var playerDTO : teamPlayers) {
            var player = this.playerRepository.findById(playerDTO.id()).orElseThrow(() -> new NotFoundUserException("Player não encontrado"));
            BigDecimal points = player.getPoints();
            points = points.add(BigDecimal.valueOf(5));
            var wins = player.getWins();
            wins += 1;
            player.setPoints(points);
            player.setWins(wins);
            var matchPlayer = player.getMatches().stream()
                    .filter(x -> Objects.equals(x.getMatchDate(), LocalDate.now()))
                    .findFirst();
            if (matchPlayer.isEmpty()) {
                var matchPlayerToSave = new MatchPlayer(null,LocalDate.now(), player, 1, 0);
                player.getMatches().add(matchPlayerToSave);
            } else {

                int winsMatches = matchPlayer.get().getWins();
                winsMatches += 1;
                matchPlayer.get().setWins(winsMatches);
            }
            player.setPlayerStatus(PlayerStatus.DEFAULT);
            this.playerRepository.save(player);
        }
        this.quantity += 1;
    }

    @Override
    public void setPointsToPlayersLossers(Set<PlayerResponseDTO> teamPlayers,  Date date) {
        System.out.println("QUANTIDADE DE VEZES QUE CHAMEI ESSE MÉTODO : " + this.quantity2);

        for (var playerDTO : teamPlayers) {
            var player = this.playerRepository.findById(playerDTO.id()).orElseThrow(() -> new NotFoundUserException("Player não encontrado"));
            BigDecimal points = player.getPoints();
            if (points.intValue() != 0) {
                points = points.subtract(BigDecimal.valueOf(5));
                player.setPoints(points);
            }
            int losses = player.getLosses();
            losses += 1;
            player.setLosses(losses);
            var matchPlayer = player.getMatches().stream()
                    .filter(x -> Objects.equals(x.getMatchDate(), LocalDate.now()))
                    .findFirst();

            if (matchPlayer.isEmpty()) {
                var matchPlayerToSave = new MatchPlayer(null, LocalDate.now(), player, 0, 1);
                player.getMatches().add(matchPlayerToSave);
            } else {
                int lossesMatches = matchPlayer.get().getLosses();
                lossesMatches += 1;
                matchPlayer.get().setLosses(lossesMatches);
            }
            player.setPlayerStatus(PlayerStatus.DEFAULT);
            this.playerRepository.save(player);
        }
        this.quantity2 += 1;

    }

    @Override
    public Boolean isStatus(Long playerId, PlayerStatus playerStatus) {
        var player = this.playerRepository.findById(playerId).orElseThrow(() -> new NotFoundUserException("Player não encontrado"));
        return player.getPlayerStatus() == playerStatus;
    }

    @Override
    public void setStatusPlayer(Long playerId, PlayerStatus status) {
        var player = this.playerRepository.findById(playerId).orElseThrow(() -> new NotFoundUserException("Player não encontrado"));
        player.setPlayerStatus(status);
        this.playerRepository.save(player);

    }

    @Override
    public void setStatusPlayer(String playerName, PlayerStatus status) {
        var player = this.playerRepository.findByUsername(playerName).orElseThrow(() -> new NotFoundUserException("Player não encontrado"));
        player.setPlayerStatus(status);
        this.playerRepository.save(player);
    }

    @Override
    public List<MatchPlayerResponseDTO> getMatchesPlayer(Long playerId) {
        var player = this.playerRepository.findById(playerId).orElseThrow(() -> new NotFoundUserException("Player não encontrado"));
        var matches = player.getMatches().stream().map(PlayerMappers::toMatchPlayerDTO).toList();
        return matches;
    }

    @Override
    public Page<PlayerResponseDTO> getPlayers(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Order.desc("points")));
        return this.playerRepository.findAll(pageable)
                .map(x -> new PlayerResponseDTO(
                        x.getId(),
                        x.getUsername(),
                        x.getWins(),
                        x.getLosses(),
                        x.getPoints(),
                        x.getUser().getId()
                ));
    }


}
