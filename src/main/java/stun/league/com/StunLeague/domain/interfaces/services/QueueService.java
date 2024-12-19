package stun.league.com.StunLeague.domain.interfaces.services;

import stun.league.com.StunLeague.domain.models.Queue;
import stun.league.com.StunLeague.domain.models.dto.player.PlayerResponseDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface QueueService {

    Queue addPlayer(PlayerResponseDTO player, UUID queueId);
    Queue getQueue();
    boolean removePlayer(UUID id, Long playerId);
    Integer getQuantityPlayersByQueueId(UUID id);
    List<String> getPlayersNames(UUID id);
    boolean inQueue(Long id);
    void confirmPlayer(Long id, UUID idQueue);
    Integer getConfirmedPlayers(UUID idQueue);
    boolean removePlayerInConfirmedPlayers(UUID id, Long playerId);
    void voteMap(String mapName, UUID queuId);
    Map<String, Integer> getVotes(UUID queuId);
    String getMapWinner(UUID queuId);

    void deleteQueue(UUID queueId);

    UUID getQueueIdByPlayerId(Long id);
}
