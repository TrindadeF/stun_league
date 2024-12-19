package stun.league.com.StunLeague.domain.interfaces.repositories.topic;

import stun.league.com.StunLeague.domain.models.dto.topics.MatchResponseTopicsDTO;

import java.util.Map;
import java.util.UUID;

public interface MatchTopicRepository {

    void addOrUpdateTopic(UUID topicId, MatchResponseTopicsDTO topic);
    MatchResponseTopicsDTO getTopic(UUID topicId);
    Map<UUID, MatchResponseTopicsDTO> getTopicsQueue();

    void delete(MatchResponseTopicsDTO topic);
}
