package stun.league.com.StunLeague.domain.models.dto.topics;

import lombok.AllArgsConstructor;
import lombok.Data;
import stun.league.com.StunLeague.domain.enums.TeamNames;
import stun.league.com.StunLeague.infra.entities.BlackKey;
import stun.league.com.StunLeague.domain.models.TeamsMatch;
import stun.league.com.StunLeague.domain.models.VotesTeam;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MatchResponseTopicsDTO {

    private UUID matchId;
    private Integer quantityPlayers;
    private Integer quantityPlayersConfirmedToStart = 0;
    private Integer votesAbort;
    private TeamsMatch teams;
    private Map<String, Integer> voteTeamWinner;
    private TeamNames teamWinner;
    private BlackKey blackKey;
    private String mapGame;
    private VotesTeam votesTeam = new VotesTeam(0, 0);
    private Boolean alreadyGetWinner = false;

    public MatchResponseTopicsDTO() {
    }
}
