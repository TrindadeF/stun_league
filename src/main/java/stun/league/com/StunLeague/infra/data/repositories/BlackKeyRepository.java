package stun.league.com.StunLeague.infra.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import stun.league.com.StunLeague.infra.entities.BlackKey;

import java.util.Optional;

@Repository
@Transactional
public interface BlackKeyRepository extends JpaRepository<BlackKey, Long> {

    @Query("SELECT k FROM BlackKey k ORDER BY k.id ASC LIMIT 1")
    Optional<BlackKey> findTopByOrderByIdAsc();
}
