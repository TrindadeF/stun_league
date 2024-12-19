package stun.league.com.StunLeague.application.services.queue;

import org.springframework.stereotype.Service;
import stun.league.com.StunLeague.domain.interfaces.repositories.queue.QueueRepository;
import stun.league.com.StunLeague.domain.interfaces.services.QueueService;
import stun.league.com.StunLeague.domain.models.Queue;
import stun.league.com.StunLeague.domain.models.dto.player.PlayerResponseDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class QueueServiceImpl implements QueueService {

    private final QueueRepository queueRepository;

    public QueueServiceImpl(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    @Override
    public Queue addPlayer(PlayerResponseDTO player, UUID queueId) {
        Optional<Queue> queue = this.queueRepository.getGetQueueById(queueId);
        queue.get().getPlayers().add(player);
        return queue.get();
    }

    @Override
    public Queue getQueue() {
        return this.queueRepository.getGetQueue().get();
    }

    @Override
    public boolean removePlayer(UUID idQueue, Long playerId) {
        var queue = this.queueRepository.getGetQueueById(idQueue);
        return queue.get().removePlayerById(playerId);
    }

    @Override
    public Integer getQuantityPlayersByQueueId(UUID id) {
        return this.queueRepository.getGetQueueById(id).get().getPlayers().size();
    }

    @Override
    public List<String> getPlayersNames(UUID id) {
        var queue = this.queueRepository.getGetQueueById(id);
        return queue.get().getPlayers().stream().map(PlayerResponseDTO::username).toList();
    }


    @Override
    public boolean inQueue(Long id) {
        return this.queueRepository.playerInQueue(id);
    }

    @Override
    public void confirmPlayer(Long idPlayer, UUID idQueue) {
        var queue = this.queueRepository.getGetQueueById(idQueue);
        queue.get().confirmPlayer(idPlayer);
    }

    @Override
    public Integer getConfirmedPlayers(UUID idQueue) {
        var queue = this.queueRepository.getGetQueueById(idQueue);
        return queue.get().getPlayersConfirmed().size();
    }

    @Override
    public boolean removePlayerInConfirmedPlayers(UUID id, Long playerId) {
        var queue = this.queueRepository.getGetQueueById(id);
        return queue.get().removeConfirmedPlayer(playerId);
    }

    @Override
    public void voteMap(String mapName, UUID queuId) {
        var queue = this.queueRepository.getGetQueueById(queuId);
        queue.get().voteMap(mapName);
    }

    @Override
    public Map<String, Integer> getVotes(UUID queuId) {
        var queue = this.queueRepository.getGetQueueById(queuId);
        return queue.get().getVotes();
    }

    @Override
    public String getMapWinner(UUID queuId) {
        var queue = this.queueRepository.getGetQueueById(queuId);
        return queue.get().getWinnerMap();
    }

    @Override
    public void deleteQueue(UUID queueId) {
        if (this.queueRepository.getGetQueueById(queueId).isPresent()) {
            this.queueRepository.removeQueueById(queueId);
        }
    }

    @Override
    public UUID getQueueIdByPlayerId(Long id) {
        return this.queueRepository.getQueueIdByPlayerId(id);
    }
}
