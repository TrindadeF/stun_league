package stun.league.com.StunLeague.domain.interfaces.repositories.topic;

import stun.league.com.StunLeague.domain.models.dto.topics.QueueResponseTopicsDTO;

import java.util.Map;
import java.util.UUID;

public interface QueueTopicRepository {

    void addOrUpdateTopic(UUID topicId, QueueResponseTopicsDTO topic);
    QueueResponseTopicsDTO getTopic(UUID topicId);
    Map<UUID, QueueResponseTopicsDTO> getTopicsQueue();

    boolean delete(QueueResponseTopicsDTO topic);
}
