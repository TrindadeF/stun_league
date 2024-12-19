package stun.league.com.StunLeague.infra.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import stun.league.com.StunLeague.infra.entities.UserConfiguration;

@Repository
@Transactional
public interface UserConfigurationRepository  extends JpaRepository<UserConfiguration, Long> {
}
