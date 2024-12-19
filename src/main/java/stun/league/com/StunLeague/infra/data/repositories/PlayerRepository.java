package stun.league.com.StunLeague.infra.data.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import stun.league.com.StunLeague.infra.entities.Player;

import java.util.Optional;

@Repository
@Transactional

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query("SELECT p FROM Player p WHERE p.username = :username OR p.user.email = :username")
    Optional<Player> findByUsername(String  username);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM Player p WHERE p.username = :username")
    boolean existByUsername(String username);

}
