package stun.league.com.StunLeague.domain.interfaces.repositories.match;

import stun.league.com.StunLeague.domain.models.Match;

import java.util.Optional;
import java.util.UUID;

public interface MatchRepository {

    Optional<Match> getMatchById(UUID id);

    Match createMatch();

    Boolean playerInMatch(Long playerId);

    void deleteById(UUID id);

    Boolean playerIsInMatch(Long playerId);

    UUID getMatchIdByPlayerId(Long id);
}
