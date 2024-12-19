package stun.league.com.StunLeague.api.controllers.websocket.queue;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import stun.league.com.StunLeague.domain.enums.PlayerStatus;
import stun.league.com.StunLeague.domain.interfaces.PlayerServices;
import stun.league.com.StunLeague.domain.interfaces.repositories.match.MatchRepository;
import stun.league.com.StunLeague.domain.interfaces.repositories.topic.MatchTopicRepository;
import stun.league.com.StunLeague.domain.interfaces.repositories.topic.QueueTopicRepository;
import stun.league.com.StunLeague.domain.interfaces.services.QueueService;
import stun.league.com.StunLeague.domain.models.dto.player.PlayerJoinQueueDTO;
import stun.league.com.StunLeague.domain.models.dto.queue.QueueRequestDTO;
import stun.league.com.StunLeague.domain.models.dto.queue.QueueVoteRequestDTO;
import stun.league.com.StunLeague.domain.models.dto.topics.MatchResponseTopicsDTO;
import stun.league.com.StunLeague.domain.models.dto.topics.QueueResponseTopicsDTO;
import stun.league.com.StunLeague.infra.data.repositories.BlackKeyRepository;

import java.util.UUID;

@Controller
public class QueueWebSocketController {

    private final QueueService queueService;
    private final SimpMessagingTemplate messagingTemplate;
    private final QueueTopicRepository topicRepository;
    private final MatchTopicRepository matchTopicRepository;
    private final MatchRepository matchRepository;
    private final PlayerServices playerServices;
    private final BlackKeyRepository blackKeyRepository;

    public QueueWebSocketController(QueueService queueService, SimpMessagingTemplate messagingTemplate, QueueTopicRepository topicRepository, MatchTopicRepository matchTopicRepository, MatchRepository matchRepository, PlayerServices playerServices, BlackKeyRepository blackKeyRepository) {
        this.queueService = queueService;
        this.messagingTemplate = messagingTemplate;
        this.topicRepository = topicRepository;
        this.matchTopicRepository = matchTopicRepository;
        this.matchRepository = matchRepository;
        this.playerServices = playerServices;
        this.blackKeyRepository = blackKeyRepository;
    }

    @MessageMapping("/join")
    public void addPlayer(PlayerJoinQueueDTO player) {
        UUID queueUUID = UUID.fromString(player.queueId().replace("\"", ""));
        this.queueService.addPlayer(player.playerResponseDTO(), queueUUID);
        QueueResponseTopicsDTO topic = this.topicRepository.getTopic(queueUUID);
        topic.setQuantityPlayers(this.queueService.getQuantityPlayersByQueueId(queueUUID));
        topic.setQueueId(queueUUID);
        topic.setQuantityPlayersConfirmedInQueue(this.queueService.getConfirmedPlayers(queueUUID));
        topic.setMapsVotes(this.queueService.getVotes(queueUUID));
        topic.setPlayersNamesInQueue(this.queueService.getPlayersNames(queueUUID));
        this.playerServices.setStatusPlayer(player.playerResponseDTO().id(), PlayerStatus.IN_QUEUE_SCREEN);
        this.topicRepository.addOrUpdateTopic(queueUUID, topic);
        String destination = "/topic/queue/" + queueUUID.toString();
        messagingTemplate.convertAndSend(destination, topic);
    }

    @MessageMapping("/get")
    public void getPlayers(String queueId) {
        UUID queueUUID = UUID.fromString(queueId.replace("\"", ""));
        QueueResponseTopicsDTO topic = this.topicRepository.getTopic(queueUUID);
        topic.setQuantityPlayers(this.queueService.getQuantityPlayersByQueueId(queueUUID));
        topic.setPlayersNamesInQueue(this.queueService.getPlayersNames(queueUUID));
        this.topicRepository.addOrUpdateTopic(queueUUID, topic);

        String destination = "/topic/queue/" + queueUUID.toString();
        messagingTemplate.convertAndSend(destination, topic);
    }

    @MessageMapping("/leave")
    public void leaveQueue(QueueRequestDTO queueLeaveDTO) {
        UUID queueUUID = UUID.fromString(queueLeaveDTO.idQueue().replace("\"", ""));
        this.queueService.removePlayer(queueUUID, queueLeaveDTO.idPlayer());
        this.queueService.removePlayerInConfirmedPlayers(queueUUID, queueLeaveDTO.idPlayer());

        QueueResponseTopicsDTO topic = this.topicRepository.getTopic(queueUUID);
        topic.setQuantityPlayers(this.queueService.getQuantityPlayersByQueueId(queueUUID));
        topic.setQuantityPlayersConfirmedInQueue(this.queueService.getConfirmedPlayers(queueUUID));
        this.topicRepository.addOrUpdateTopic(queueUUID, topic);
        String destination = "/topic/queue/" + queueUUID.toString();
        messagingTemplate.convertAndSend(destination, topic);
    }

    @MessageMapping("/confirm")
    public void confirmQueue(QueueRequestDTO confirmDTO) {
        UUID queueUUID = UUID.fromString(confirmDTO.idQueue().replace("\"", ""));
        this.queueService.confirmPlayer(confirmDTO.idPlayer(), queueUUID);
        QueueResponseTopicsDTO topic = this.topicRepository.getTopic(queueUUID);
        topic.setQuantityPlayersConfirmedInQueue(this.queueService.getConfirmedPlayers(queueUUID));
        this.topicRepository.addOrUpdateTopic(queueUUID, topic);
        String destination = "/topic/queue/" + queueUUID.toString();
        messagingTemplate.convertAndSend(destination, topic);
        System.out.println(this.topicRepository.getTopicsQueue().entrySet());
   }

    @MessageMapping("/vote-map")
    public void voteMap(QueueVoteRequestDTO voteRequestDTO) {
        UUID queueUUID = UUID.fromString(voteRequestDTO.queueId().replace("\"", ""));
        String cleanedMapName = voteRequestDTO.nameMap().replace("\"", "");
        this.queueService.voteMap(cleanedMapName, queueUUID);
        QueueResponseTopicsDTO topic = this.topicRepository.getTopic(queueUUID);
        topic.setMapsVotes(this.queueService.getVotes(queueUUID));
        this.topicRepository.addOrUpdateTopic(queueUUID, topic);
        String destination = "/topic/queue/" + queueUUID.toString();
        messagingTemplate.convertAndSend(destination, topic);
    }

    @MessageMapping("/get-winner-map")
    public void getMapWinner(String queueId) {
        UUID queueUUID = UUID.fromString(queueId.replace("\"", ""));
        String mapWinner = this.queueService.getMapWinner(queueUUID);
        QueueResponseTopicsDTO topic = this.topicRepository.getTopic(queueUUID);
        topic.setMapWinner(mapWinner);
        this.topicRepository.addOrUpdateTopic(queueUUID, topic);

        if (topic.getMatchId() == null) {
            var match = this.matchRepository.createMatch();
            var topicMatch = new MatchResponseTopicsDTO();
            topicMatch.setMatchId(match.getId());
            topicMatch.setMapGame(mapWinner);
            if (topicMatch.getBlackKey() == null) {
                var blackKey = this.blackKeyRepository.findTopByOrderByIdAsc();
                blackKey.ifPresent(topicMatch::setBlackKey);
                this.blackKeyRepository.deleteById(blackKey.get().getId());
            }
            topic.setMatchId(match.getId());
            for (var playerName : topic.getPlayersNamesInQueue()) {
                match.addPlayerToPlayers(this.playerServices.findByUsername(playerName));
                this.playerServices.setStatusPlayer(playerName, PlayerStatus.IN_ANT_CHEATER_SCREEN);
            }
            this.matchTopicRepository.addOrUpdateTopic(match.getId(),topicMatch);
            String destinationMatchTopic = "/topic/match/" + match.getId().toString();
            messagingTemplate.convertAndSend(destinationMatchTopic, topicMatch);
        }
        String destinationQueueTopic = "/topic/queue/" + queueUUID.toString();
        messagingTemplate.convertAndSend(destinationQueueTopic, topic);

        this.queueService.deleteQueue(queueUUID);
        this.topicRepository.delete(topic);

    }

}
