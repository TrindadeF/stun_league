package stun.league.com.StunLeague.infra.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "match_players")
@AllArgsConstructor
@Data
public class MatchPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "match_date")
    private LocalDate matchDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_player")
    @JsonBackReference
    private Player player;
    private Integer wins;
    private Integer losses;

    public MatchPlayer() {
    }
}
