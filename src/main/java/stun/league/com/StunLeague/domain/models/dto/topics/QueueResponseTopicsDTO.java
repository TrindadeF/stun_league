package stun.league.com.StunLeague.domain.models.dto.topics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class QueueResponseTopicsDTO {

    private UUID queueId;
    private Integer quantityPlayers;
    private Integer quantityPlayersConfirmedInQueue;
    private Map<String, Integer> mapsVotes;
    private String mapWinner = "";
    private UUID matchId;

    List<String> playersNamesInQueue;

    public QueueResponseTopicsDTO() {
    }
}
