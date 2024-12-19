package stun.league.com.StunLeague.infra.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import stun.league.com.StunLeague.infra.entities.MatchPlayer;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@Transactional
public interface MatchPlayerRepository extends JpaRepository<MatchPlayer, Long> {


    @Query("SELECT mp FROM MatchPlayer mp WHERE mp.matchDate = :date AND mp.player.id = :playerId")
    Optional<MatchPlayer> findByMatchDateAndPlayerId(@Param("date") LocalDate date, @Param("playerId") Long playerId);
}
