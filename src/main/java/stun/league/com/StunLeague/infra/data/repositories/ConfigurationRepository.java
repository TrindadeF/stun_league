package stun.league.com.StunLeague.infra.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import stun.league.com.StunLeague.infra.entities.Configuration;

@Repository
@Transactional
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
}
