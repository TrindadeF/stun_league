package stun.league.com.StunLeague.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import stun.league.com.StunLeague.domain.models.dto.player.PlayerResponseDTO;

import java.util.*;

@Data
@AllArgsConstructor
public class Queue {

    private UUID id;
    private Set<PlayerResponseDTO> players;
    private Set<Long> playersConfirmed;
    private Map<String, Integer> mapsVotes;


    public Queue(UUID uuid) {
        this.players = new HashSet<>();
        this.playersConfirmed = new HashSet<>();
        this.mapsVotes = new HashMap<>();
        this.id = uuid;
        this.mapsVotes.put("viuva-t", 0);
        this.mapsVotes.put("porto-t", 0);
        this.mapsVotes.put("ankara-t", 0);
        this.mapsVotes.put("satelite-t", 0);
        this.mapsVotes.put("mexico-t", 0);
        this.mapsVotes.put("olho-agua-2.0-t", 0);
    }


    public boolean removePlayerById(Long idPlayer) {
        for (var player: players) {
            if (player.id().equals(idPlayer)) {
                return players.remove(player);
            }
        }
        return false;
    }

    public Boolean playerInQueue(Long id) {
        for (var player: players) {
            if (player.id().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void confirmPlayer(Long id) {
        for (var p: this.players) {
            if (p.id().equals(id)) {
                this.playersConfirmed.add(id);
            }
        }
    }

    public boolean removeConfirmedPlayer(Long id) {
        return this.playersConfirmed.remove(id);
    }

    public void voteMap(String mapName) {
        this.mapsVotes.put(mapName, this.mapsVotes.get(mapName) + 1);
    }

    public Map<String, Integer> getVotes() {
        return this.mapsVotes;
    }

    public String getWinnerMap() {
        String maxKey = null;
        Integer maxValue = null;

        for (Map.Entry<String, Integer> entry : this.mapsVotes.entrySet()) {
            if (maxValue == null || entry.getValue().compareTo(maxValue) > 0) {
                maxValue = entry.getValue();
                maxKey = entry.getKey();
            }
        }

        return maxKey;
    }

}

