package stun.league.com.StunLeague.domain.interfaces.repositories.queue;


import stun.league.com.StunLeague.domain.models.Queue;

import java.util.Optional;
import java.util.UUID;


public interface QueueRepository {

    Queue createQueue();
    Optional<Queue> getGetQueueById(UUID uuid);
    Boolean removeQueueById(UUID uuid);

    Optional<Queue> getGetQueue();

    Boolean playerInQueue(Long id);

    UUID getQueueIdByPlayerId(Long id);
}
