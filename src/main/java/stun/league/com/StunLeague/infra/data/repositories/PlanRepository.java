package stun.league.com.StunLeague.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stun.league.com.StunLeague.domain.models.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

}
