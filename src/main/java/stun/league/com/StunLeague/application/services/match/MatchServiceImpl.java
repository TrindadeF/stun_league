package stun.league.com.StunLeague.application.services.match;

import org.springframework.stereotype.Component;
import stun.league.com.StunLeague.domain.enums.TeamNames;
import stun.league.com.StunLeague.domain.exceptions.MatchNotFoundException;
import stun.league.com.StunLeague.domain.interfaces.repositories.match.MatchRepository;
import stun.league.com.StunLeague.domain.interfaces.services.MatchService;
import stun.league.com.StunLeague.domain.models.Match;
import stun.league.com.StunLeague.domain.models.TeamsMatch;
import stun.league.com.StunLeague.domain.models.VotesTeam;
import stun.league.com.StunLeague.domain.models.dto.player.PlayerResponseDTO;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class MatchServiceImpl  implements MatchService {

    private final MatchRepository matchRepository;

    public MatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public boolean addPlayerToPlayersList(PlayerResponseDTO player, UUID matchId) {
        var match = this.matchRepository.getMatchById(matchId).orElseThrow(()  ->  new MatchNotFoundException("Queue not found!"));
        return match.addPlayerToPlayers(player);
    }

    @Override
    public boolean removePlayerToPlayerList(PlayerResponseDTO player, UUID matchId) {
        var match = this.matchRepository.getMatchById(matchId).orElseThrow(()  ->  new MatchNotFoundException("Queue not found!"));
        return match.removePlayer(player);
    }

    @Override
    public Match getMatch(UUID id) {
        var match = this.matchRepository.getMatchById(id).orElseThrow(() -> new MatchNotFoundException("Match not found!"));
        return match;
    }

    @Override
    public TeamsMatch getTeams(UUID id) {
        var match = this.matchRepository.getMatchById(id).orElseThrow(() -> new MatchNotFoundException("Match not found!"));
        var players = new java.util.ArrayList<>(match.getPlayers().stream().toList());
        var quantityPlayersEachTeam = players.size() / 2;
        var teams = match.getTeams();

        if (teams == null) {
            Collections.shuffle(players);

            teams = new TeamsMatch();
            Set<PlayerResponseDTO> teamBL = new HashSet<>();
            Set<PlayerResponseDTO> teamGR = new HashSet<>();

            for (int i = 0; i < quantityPlayersEachTeam; i++) {
                teamBL.add(players.get(i));
            }

            for (int i = quantityPlayersEachTeam; i < players.size(); i++) {
                teamGR.add(players.get(i));
            }

            teams.setBL(teamBL);
            teams.setGR(teamGR);
            match.setTeams(teams);

        }

        return teams;
    }

    @Override
    public boolean inMatch(Long playerId) {
        return this.matchRepository.playerInMatch(playerId);
    }

    @Override
    public void voteToAbort(UUID matchId, Long playerId) {
        var match = this.matchRepository.getMatchById(matchId).orElseThrow(() -> new MatchNotFoundException("Match not found!"));
        match.voteToAbort(playerId);
    }

    @Override
    public Integer getVotesToAbort(UUID matchId) {
        var match = this.matchRepository.getMatchById(matchId).orElseThrow(() -> new MatchNotFoundException("Match not found!"));
        return match.getVotesToAbort();
    }

    @Override
    public void resetVotesToAbort(UUID matchId) {
        var match = this.matchRepository.getMatchById(matchId).orElseThrow(() -> new MatchNotFoundException("Match not found!"));
        match.getPlayersVotedToAbort().clear();
    }

    @Override
    public void deleteById(UUID matchId) {
        var match = this.matchRepository.getMatchById(matchId).orElseThrow(() -> new MatchNotFoundException("Match not found!"));
        this.matchRepository.deleteById(match.getId());
    }

    @Override
    public VotesTeam voteTeam(UUID matchId, TeamNames teamName, Long playerId) {
        var match = this.matchRepository.getMatchById(matchId).orElseThrow(() -> new MatchNotFoundException("Match not found!"));
        return match.voteTeam(teamName, playerId);
    }

    @Override
    public TeamNames getWinnerMatch(UUID matchId) {
        var match = this.matchRepository.getMatchById(matchId).orElseThrow(() -> new MatchNotFoundException("Match not found!"));
        return match.getTeamWinner();
    }

    @Override
    public TeamNames getLosserMatch(UUID matchId) {
        var match = this.matchRepository.getMatchById(matchId).orElseThrow(() -> new MatchNotFoundException("Match not found!"));
        return match.getTeamLosser();
    }

    @Override
    public Set<PlayerResponseDTO> getTeamPlayers(TeamNames team, UUID matchId) {
        var match = this.matchRepository.getMatchById(matchId).orElseThrow(() -> new MatchNotFoundException("Match not found!"));
        return match.getPlayersTeam(team);
    }

    @Override
    public Boolean playerInQueue(Long playerId) {
        return this.matchRepository.playerIsInMatch(playerId);
    }

    @Override
    public UUID getMatchIdByPlayerId(Long id) {
        return this.matchRepository.getMatchIdByPlayerId(id);
    }

    @Override
    public void resetVotesTeams(UUID matchUUID) {
        var match = this.matchRepository.getMatchById(matchUUID).orElseThrow(() -> new MatchNotFoundException("Match not found!"));
        match.getVotesTeams().clear();
        match.getPlayersAlreadyVotedTeams().clear();
        match.setVotesTeam(new VotesTeam(0, 0));
        match.getVotesTeams().put(TeamNames.GR, new HashSet<>());
        match.getVotesTeams().put(TeamNames.BL, new HashSet<>());
    }
}
