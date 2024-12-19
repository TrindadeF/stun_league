package stun.league.com.StunLeague.infra.data.repositories.topics;

import org.springframework.stereotype.Component;
import stun.league.com.StunLeague.domain.interfaces.repositories.topic.MatchTopicRepository;
import stun.league.com.StunLeague.domain.models.dto.topics.MatchResponseTopicsDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MatchTopicRepositoryImpl implements MatchTopicRepository {

    private Map<UUID, MatchResponseTopicsDTO> topics;

    public MatchTopicRepositoryImpl() {
        this.topics = new HashMap<>();
    }

    @Override
    public void addOrUpdateTopic(UUID topicId, MatchResponseTopicsDTO topic) {
        this.topics.put(topicId, topic);
    }

    @Override
    public MatchResponseTopicsDTO getTopic(UUID topicId) {
        return this.topics.getOrDefault(topicId, new MatchResponseTopicsDTO());
    }

    @Override
    public Map<UUID, MatchResponseTopicsDTO> getTopicsQueue() {
        return this.topics;
    }

    @Override
    public void delete(MatchResponseTopicsDTO topic) {
        this.topics.remove(topic.getMatchId());
    }
}
