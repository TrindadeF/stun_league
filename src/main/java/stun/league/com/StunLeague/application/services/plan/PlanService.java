package stun.league.com.StunLeague.domain.services;

import org.springframework.stereotype.Service;
import stun.league.com.StunLeague.domain.models.Plan;
import stun.league.com.StunLeague.domain.repositories.PlanRepository;

import java.util.List;

@Service
public class PlanService {
    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    public Plan getPlanById(Long id) {
        return planRepository.findById(id).orElseThrow(() -> new RuntimeException("Plano não encontrado!"));
    }
}
