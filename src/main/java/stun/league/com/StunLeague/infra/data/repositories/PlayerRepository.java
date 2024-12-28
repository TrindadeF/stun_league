package stun.league.com.StunLeague.infra.data.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import stun.league.com.StunLeague.infra.entities.Player;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query("SELECT p FROM Player p WHERE p.username = :username")
    Optional<Player> findByUsername(String username);

    @Query("SELECT p FROM Player p WHERE p.username = :usernameOrEmail OR p.user.email = :usernameOrEmail")
    Optional<Player> findByUsernameOrEmail(String usernameOrEmail);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM Player p WHERE p.username = :username")
    boolean existByUsername(String username);

    @Query("SELECT p FROM Player p ORDER BY p.points DESC")
    List <Player> findTop10ByPoints();
}
