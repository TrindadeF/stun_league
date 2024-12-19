package stun.league.com.StunLeague.infra.data.repositories;

import org.springframework.stereotype.Component;
import stun.league.com.StunLeague.domain.interfaces.repositories.match.MatchRepository;
import stun.league.com.StunLeague.domain.models.Match;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class MatchRepositoryImpl implements MatchRepository {

    private final Map<UUID, Match> matches;

    public MatchRepositoryImpl() {
        this.matches = new HashMap<>();
    }

    @Override
    public Optional<Match> getMatchById(UUID id) {

        return Optional.of(this.matches.get(id));
    }

    @Override
    public Match createMatch() {
        Match match = new Match();
        var id = UUID.randomUUID();
        match.setId(id);
        this.matches.put(id, match);
        return match;
    }

    @Override
    public Boolean playerInMatch(Long id) {
        for (var match : matches.values()) {
            if (match.playerInMatch(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteById(UUID id) {
        this.matches.remove(id);
    }

    @Override
    public Boolean playerIsInMatch(Long playerId) {
        for (var match : this.matches.values()) {
            for (var player : match.getPlayers()) {
                if (player.id().equals(playerId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public UUID getMatchIdByPlayerId(Long id) {
        for (var match : this.matches.values()) {
            for (var player : match.getPlayers()) {
                if (player.id().equals(id)) {
                    return match.getId();
                }
            }
        }
        return null;
    }

}
