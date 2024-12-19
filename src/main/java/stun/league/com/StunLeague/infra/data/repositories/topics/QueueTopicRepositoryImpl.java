package stun.league.com.StunLeague.infra.data.repositories.topics;

import org.springframework.stereotype.Component;
import stun.league.com.StunLeague.domain.interfaces.repositories.topic.QueueTopicRepository;
import stun.league.com.StunLeague.domain.models.dto.topics.QueueResponseTopicsDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class QueueTopicRepositoryImpl implements QueueTopicRepository {

    private final Map<UUID, QueueResponseTopicsDTO> topics;

    public QueueTopicRepositoryImpl() {
        this.topics = new HashMap<>();
    }


    @Override
    public void addOrUpdateTopic(UUID topicId, QueueResponseTopicsDTO topic) {
        this.topics.put(topicId,  topic);
    }

    @Override
    public QueueResponseTopicsDTO getTopic(UUID topicId) {
        return this.topics.getOrDefault(topicId, new QueueResponseTopicsDTO());
    }


    @Override
    public Map<UUID, QueueResponseTopicsDTO> getTopicsQueue() {
        return this.topics;
    }

    @Override
    public boolean delete(QueueResponseTopicsDTO topic) {
        this.topics.remove(topic.getQueueId());
        return false;
    }

}
