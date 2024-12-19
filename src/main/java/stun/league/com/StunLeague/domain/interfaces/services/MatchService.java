package stun.league.com.StunLeague.domain.interfaces.services;

import stun.league.com.StunLeague.domain.enums.TeamNames;
import stun.league.com.StunLeague.domain.models.Match;
import stun.league.com.StunLeague.domain.models.TeamsMatch;
import stun.league.com.StunLeague.domain.models.VotesTeam;
import stun.league.com.StunLeague.domain.models.dto.player.PlayerResponseDTO;

import java.util.Set;
import java.util.UUID;

public interface MatchService {

    boolean addPlayerToPlayersList(PlayerResponseDTO player, UUID matchId);

    boolean removePlayerToPlayerList(PlayerResponseDTO player, UUID matchId);

    Match getMatch(UUID id);

    TeamsMatch getTeams(UUID id);

    boolean inMatch(Long playerId);

    void voteToAbort(UUID matchId, Long playerId);

    Integer getVotesToAbort(UUID matchId);

    void resetVotesToAbort(UUID matchId);

    void deleteById(UUID matchUUID);

    VotesTeam voteTeam(UUID matchUUID, TeamNames teamNames, Long playerId);

    TeamNames getWinnerMatch(UUID matchUUID);

    TeamNames getLosserMatch(UUID matchUUID);

    Set<PlayerResponseDTO> getTeamPlayers(TeamNames teamWinner, UUID matchId);

    Boolean playerInQueue(Long playerId);

    UUID getMatchIdByPlayerId(Long id);

    void resetVotesTeams(UUID matchUUID);
}
