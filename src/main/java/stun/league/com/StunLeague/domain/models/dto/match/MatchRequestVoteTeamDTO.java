package stun.league.com.StunLeague.domain.models.dto.match;

import stun.league.com.StunLeague.domain.enums.TeamNames;

public record MatchRequestVoteTeamDTO(String matchId, TeamNames teamName, Long playerId) {
}
