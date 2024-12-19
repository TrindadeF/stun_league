package stun.league.com.StunLeague.api.controllers.websocket.match;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import stun.league.com.StunLeague.domain.enums.PlayerStatus;
import stun.league.com.StunLeague.domain.interfaces.PlayerServices;
import stun.league.com.StunLeague.domain.interfaces.repositories.topic.MatchTopicRepository;
import stun.league.com.StunLeague.domain.interfaces.services.MatchService;
import stun.league.com.StunLeague.domain.models.VotesTeam;
import stun.league.com.StunLeague.domain.models.dto.match.MatchCancelRequestDTO;
import stun.league.com.StunLeague.domain.models.dto.match.MatchRequestDTO;
import stun.league.com.StunLeague.domain.models.dto.match.MatchRequestVoteTeamDTO;
import stun.league.com.StunLeague.domain.models.dto.topics.MatchResponseTopicsDTO;
import stun.league.com.StunLeague.infra.data.repositories.BlackKeyRepository;
import stun.league.com.StunLeague.infra.entities.BlackKey;

import java.util.Date;
import java.util.UUID;

@Controller
public class MatchWebSocketController {

    private final MatchTopicRepository matchTopicRepository;
    private final MatchService matchService;
    private final SimpMessagingTemplate messagingTemplate;
    private final BlackKeyRepository blackKeyRepository;
    private final PlayerServices playerService;

    public MatchWebSocketController(MatchTopicRepository matchTopicRepository, MatchService matchService, SimpMessagingTemplate messagingTemplate, BlackKeyRepository blackKeyRepository, PlayerServices playerService) {
        this.matchTopicRepository = matchTopicRepository;
        this.matchService = matchService;
        this.messagingTemplate = messagingTemplate;
        this.blackKeyRepository = blackKeyRepository;
        this.playerService = playerService;
    }

//    @MessageMapping("/join-match")
//    public void joinMatch(PlayerJoinQueueDTO playerJoinQueueDTO) {
//        UUID matchUUID = UUID.fromString(playerJoinQueueDTO.queueId().replace("\"", ""));
//        MatchResponseTopicsDTO topic = this.matchTopicRepository.getTopic(matchUUID);
//        this.matchService.addPlayerToPlayersList(playerJoinQueueDTO.playerResponseDTO(), matchUUID);
//        if (topic.getBlackKey() == null) {
//            BlackKey blackKey = this.blackKeyRepository.findTopByOrderByIdAsc().orElseThrow(() -> new RuntimeException("Nenhuma key encontrada"));
//            topic.setBlackKey(blackKey);
//        }
////        if (this.blackKeyRepository.findById(topic.getBlackKey().getId()).isPresent()) {
////            this.blackKeyRepository.deleteById(topic.getBlackKey().getId());
////        }
////        match.addPlayerToPlayers(playerJoinQueueDTO.playerResponseDTO());
//        topic.setQuantityPlayers(this.matchService.getMatch(matchUUID).getPlayers().size());
//        this.matchTopicRepository.addOrUpdateTopic(matchUUID, topic);
//
//        String destinationQueueTopic = "/topic/match/" + matchUUID.toString();
//        messagingTemplate.convertAndSend(destinationQueueTopic, topic);
//
//    }
    @MessageMapping("/update")
    public void update(MatchRequestDTO matchRequestDTO) {
        UUID matchUUID = UUID.fromString(matchRequestDTO.matchId().replace("\"", ""));
        MatchResponseTopicsDTO topic = this.matchTopicRepository.getTopic(matchUUID);
        String destinationQueueTopic = "/topic/match/" + matchUUID.toString();
        messagingTemplate.convertAndSend(destinationQueueTopic, topic);
    }

    @MessageMapping("/start")
    public void start(MatchRequestDTO matchRequestDTO) {
        UUID matchUUID = UUID.fromString(matchRequestDTO.matchId().replace("\"", ""));
        MatchResponseTopicsDTO topic = this.matchTopicRepository.getTopic(matchUUID);
        Integer playersConfirmed = topic.getQuantityPlayersConfirmedToStart();
        topic.setQuantityPlayersConfirmedToStart(playersConfirmed += 1);
        this.playerService.setStatusPlayer(matchRequestDTO.playerId(), PlayerStatus.IN_MATCH_SCREEN);
        this.matchTopicRepository.addOrUpdateTopic(matchUUID, topic);
        String destinationQueueTopic = "/topic/match/" + matchUUID.toString();
        messagingTemplate.convertAndSend(destinationQueueTopic, topic);
    }

    @MessageMapping("/cancel")
    public void start(MatchCancelRequestDTO requestDTO) {
        UUID matchUUID = UUID.fromString(requestDTO.matchId().replace("\"", ""));
        MatchResponseTopicsDTO topic = this.matchTopicRepository.getTopic(matchUUID);
        Integer playersConfirmed = topic.getQuantityPlayersConfirmedToStart();
        topic.setQuantityPlayersConfirmedToStart(playersConfirmed -= 1);
        this.matchService.removePlayerToPlayerList(requestDTO.playerResponseDTO(), matchUUID);
        topic.setQuantityPlayers(this.matchService.getMatch(matchUUID).getPlayers().size());
        this.playerService.setStatusPlayer(requestDTO.playerResponseDTO().id(), PlayerStatus.DEFAULT);
        this.matchTopicRepository.addOrUpdateTopic(matchUUID, topic);
        String destinationQueueTopic = "/topic/match/" + matchUUID.toString();
        messagingTemplate.convertAndSend(destinationQueueTopic, topic);
    }

    @MessageMapping("/get-teams")
    public void getTeams(String matchId) {
        UUID matchUUID = UUID.fromString(matchId.replace("\"", ""));
        MatchResponseTopicsDTO topic = this.matchTopicRepository.getTopic(matchUUID);
        if (topic.getBlackKey() == null) {
            BlackKey blackKey = this.blackKeyRepository.findTopByOrderByIdAsc().orElseThrow(() -> new RuntimeException("Nenhuma key encontrada"));
            topic.setBlackKey(blackKey);
        }
        var teams = this.matchService.getTeams(matchUUID);
        topic.setTeams(teams);
        topic.setQuantityPlayers(this.matchService.getMatch(matchUUID).getPlayers().size());
        this.matchTopicRepository.addOrUpdateTopic(matchUUID, topic);
        String destinationQueueTopic = "/topic/match/" + matchUUID.toString();
        messagingTemplate.convertAndSend(destinationQueueTopic, topic);
    }

    @MessageMapping("/vote-abort")
    public void voteToAbort(MatchRequestDTO matchRequestDTO) {
        UUID matchUUID = UUID.fromString(matchRequestDTO.matchId().replace("\"", ""));
        MatchResponseTopicsDTO topic = this.matchTopicRepository.getTopic(matchUUID);
        this.matchService.voteToAbort(matchUUID, matchRequestDTO.playerId());
        Integer votes = this.matchService.getVotesToAbort(matchUUID);
        topic.setVotesAbort(votes);
        this.matchTopicRepository.addOrUpdateTopic(matchUUID, topic);
        String destinationQueueTopic = "/topic/match/" + matchUUID.toString();
        messagingTemplate.convertAndSend(destinationQueueTopic, topic);
    }

    @MessageMapping("/vote-team")
    public void voteTeam(MatchRequestVoteTeamDTO matchRequestDTO) {
        UUID matchUUID = UUID.fromString(matchRequestDTO.matchId().replace("\"", ""));
        MatchResponseTopicsDTO topic = this.matchTopicRepository.getTopic(matchUUID);
        var votesTeam = this.matchService.voteTeam(matchUUID, matchRequestDTO.teamName(), matchRequestDTO.playerId());
        topic.setVotesTeam(votesTeam);
        this.matchTopicRepository.addOrUpdateTopic(matchUUID, topic);
        String destinationQueueTopic = "/topic/match/" + matchUUID.toString();
        messagingTemplate.convertAndSend(destinationQueueTopic, topic);
    }

    @MessageMapping("/get-match-winner")
    public void getWinnerMatch(String matchId) {
        UUID matchUUID = UUID.fromString(matchId.replace("\"", ""));
        String destinationQueueTopic = "/topic/match/" + matchUUID.toString();
        MatchResponseTopicsDTO topic = this.matchTopicRepository.getTopic(matchUUID);
        if (topic.getMatchId() != null) {

            if (!topic.getAlreadyGetWinner()) {
                var teamWinner = this.matchService.getWinnerMatch(matchUUID);
                var teamLosser = this.matchService.getLosserMatch(matchUUID);
                topic.setTeamWinner(teamWinner);
                this.matchTopicRepository.addOrUpdateTopic(matchUUID, topic);
                Date date = new Date();
                this.playerService.setPointsToPlayersWinners(this.matchService.getTeamPlayers(teamWinner, matchUUID), date);
                this.playerService.setPointsToPlayersLossers(this.matchService.getTeamPlayers(teamLosser, matchUUID), date);
                topic.setAlreadyGetWinner(true);
                this.matchTopicRepository.addOrUpdateTopic(matchUUID, topic);
                this.matchTopicRepository.delete(topic);
                this.matchService.deleteById(matchUUID);
            }

            messagingTemplate.convertAndSend(destinationQueueTopic, topic);
        }
    }



    @MessageMapping("/reset-votes-abort")
    public void reset(String matchId) {
        UUID matchUUID = UUID.fromString(matchId.replace("\"", ""));
        MatchResponseTopicsDTO topic = this.matchTopicRepository.getTopic(matchUUID);
        this.matchService.resetVotesToAbort(matchUUID);
        Integer votes = this.matchService.getVotesToAbort(matchUUID);
        topic.setVotesAbort(votes);
        this.matchTopicRepository.addOrUpdateTopic(matchUUID, topic);
        String destinationQueueTopic = "/topic/match/" + matchUUID.toString();
        messagingTemplate.convertAndSend(destinationQueueTopic, topic);

    }

    @MessageMapping("/reset-votes-teams")
    public void resetVotesTeam(String matchId) {
        UUID matchUUID = UUID.fromString(matchId.replace("\"", ""));
        MatchResponseTopicsDTO topic = this.matchTopicRepository.getTopic(matchUUID);
        this.matchService.resetVotesTeams(matchUUID);
        topic.setVotesTeam(new VotesTeam(0,0));
        this.matchTopicRepository.addOrUpdateTopic(matchUUID, topic);
        String destinationQueueTopic = "/topic/match/" + matchUUID.toString();
        messagingTemplate.convertAndSend(destinationQueueTopic, topic);

    }

    @MessageMapping("cancel-match")
    public void cancel(String matchId) {
        UUID matchUUID = UUID.fromString(matchId.replace("\"", ""));
        MatchResponseTopicsDTO topic = this.matchTopicRepository.getTopic(matchUUID);

        for (var gr : topic.getTeams().getGR()) {
            this.playerService.setStatusPlayer(gr.id(), PlayerStatus.DEFAULT);
        }

        for (var bl : topic.getTeams().getBL()) {
            this.playerService.setStatusPlayer(bl.id(), PlayerStatus.DEFAULT);
        }
        this.matchTopicRepository.delete(topic);
        this.matchService.deleteById(matchUUID);
    }

}


