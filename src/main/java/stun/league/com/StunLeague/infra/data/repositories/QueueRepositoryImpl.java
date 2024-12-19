package stun.league.com.StunLeague.infra.data.repositories;

import org.springframework.stereotype.Component;
import stun.league.com.StunLeague.domain.interfaces.repositories.queue.QueueRepository;
import stun.league.com.StunLeague.domain.models.Queue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class QueueRepositoryImpl implements QueueRepository {
    private final Map<UUID, Queue> queues;

    public QueueRepositoryImpl() {
        this.queues = new HashMap<>();
    }

    @Override
    public Queue createQueue() {
        Queue queue = new Queue(UUID.randomUUID());
        this.queues.put(queue.getId(), queue);
        return queue;
    }

    @Override
    public Optional<Queue> getGetQueueById(UUID uuid) {
        return Optional.of(this.queues.get(uuid));
    }

    @Override
    public Boolean removeQueueById(UUID uuid) {
        return this.queues.remove(uuid) != null;
    }

    @Override
    public Optional<Queue> getGetQueue() {
        for (var queue : queues.values()) {
            if (queue.getPlayers().size() < 2) {
                return Optional.of(queue);
            }
        }
        return Optional.of(this.createQueue());
    }

    @Override
    public Boolean playerInQueue(Long id) {
        for (var queue : queues.values()) {
            if (queue.playerInQueue(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public UUID getQueueIdByPlayerId(Long id) {
        for (var queue : this.queues.values()) {
            for (var player : queue.getPlayers()) {
                if (player.id().equals(id)) {
                    return queue.getId();
                }
            }
        }
        return null;
    }
}
