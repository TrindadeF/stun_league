package stun.league.com.StunLeague.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import stun.league.com.StunLeague.domain.enums.TeamNames;
import stun.league.com.StunLeague.domain.models.dto.player.PlayerResponseDTO;

import java.util.*;

@Data
@AllArgsConstructor
public class Match {

    private UUID id;
    private Set<PlayerResponseDTO> players;
    private TeamsMatch teams;
    private Set<Long> playersVotedToAbort;
    private Map<TeamNames, Set<Long>> votesTeams;
    private VotesTeam votesTeam;
    private Set<Long> playersAlreadyVotedTeams;

    public Match() {
        this.players = new HashSet<>();
        this.playersVotedToAbort = new HashSet<>();
        this.votesTeams = new HashMap<>();
        this.votesTeams.put(TeamNames.GR, new HashSet<>());
        this.votesTeams.put(TeamNames.BL, new HashSet<>());
        this.playersAlreadyVotedTeams  = new HashSet<>();
    }

    public boolean addPlayerToPlayers(PlayerResponseDTO playerResponseDTO) {
        return this.players.add(playerResponseDTO);
    }

    public boolean removePlayer(PlayerResponseDTO playerResponseDTO) {
        return this.players.remove(playerResponseDTO);
    }


    public Boolean playerInMatch(Long id) {
        for (var player: players) {
            if (player.id().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void voteToAbort(Long playerId) {
        this.playersVotedToAbort.add(playerId);
    }

    public Integer getVotesToAbort() {
        return this.playersVotedToAbort.size();
    }

    public VotesTeam voteTeam(TeamNames teamName, Long playerId) {
        if (!this.playersAlreadyVotedTeams.contains(playerId)) {
            var votes = this.votesTeams.getOrDefault(teamName, new HashSet<>());
            votes.add(playerId);
            this.votesTeams.put(teamName, votes);
            var votesBL = this.votesTeams.getOrDefault(TeamNames.BL, new HashSet<>()).size();
            var votesGR = this.votesTeams.getOrDefault(TeamNames.GR, new HashSet<>()).size();

            if (this.votesTeam != null) {
                this.votesTeam.setVotesTeamBL(this.votesTeams.get(TeamNames.BL).size());
                this.votesTeam.setVotesTeamGR(this.votesTeams.get(TeamNames.GR).size());
            } else {
                this.votesTeam = new VotesTeam(votesGR, votesBL);
            }
            this.playersAlreadyVotedTeams.add(playerId);
        }
        return this.votesTeam;

    }

    public Integer getVotesTeam(TeamNames teamName) {
        return this.votesTeams.get(teamName).size();
    }

    public TeamNames getTeamWinner() {
        TeamNames teamWithMostVotes = null;
        int maxVotes = 0;

        for (Map.Entry<TeamNames, Set<Long>> entry : this.votesTeams.entrySet()) {
            int voteCount = entry.getValue().size();
            if (voteCount > maxVotes) {
                maxVotes = voteCount;
                teamWithMostVotes = entry.getKey();
            }
        }
        return teamWithMostVotes;
    }

    public TeamNames getTeamLosser() {
        TeamNames teamWithLeastVotes = null;
        int minVotes = Integer.MAX_VALUE; // Iniciar com o maior valor possível

        for (Map.Entry<TeamNames, Set<Long>> entry : this.votesTeams.entrySet()) {
            int voteCount = entry.getValue().size();
            if (voteCount < minVotes) {
                minVotes = voteCount;
                teamWithLeastVotes = entry.getKey();
            }
        }
        return teamWithLeastVotes;
    }

    public Set<PlayerResponseDTO> getPlayersTeam(TeamNames team) {
        return switch (team) {
            case BL -> this.teams.getBL();
            case GR -> this.teams.getGR();
        };
    }
}

