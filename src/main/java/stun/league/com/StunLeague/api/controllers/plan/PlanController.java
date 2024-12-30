import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stun.league.com.StunLeague.domain.models.dto.plan.PlanDTO;
import stun.league.com.StunLeague.application.services.user.UserServicesImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/plans")
public class PlanController {

    private final UserServicesImpl userServices;

    public PlanController(UserServicesImpl userServices) {
        this.userServices = userServices;
    }

    @GetMapping
    public ResponseEntity<List<PlanDTO>> getUserPlans(@RequestHeader("Authorization") String token) {
        Long userId = extractUserIdFromToken(token);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userServices.findById(userId);

        List<PlanDTO> plans = new ArrayList<>();
        if ("FREE".equals(user.subscription())) {
            plans.add(new PlanDTO("FREE", List.of("Benefício 1", "Benefício 2", "Benefício 3"), "Seu plano atual", 0));
            plans.add(new PlanDTO("PRO", List.of("Benefício 1", "Benefício 2", "Benefício 3", "Benefício 4"), "R$35,00/mês", 35));
        } else if ("PRO".equals(user.subscription())) {
            plans.add(new PlanDTO("PRO", List.of("Benefício 1", "Benefício 2", "Benefício 3", "Benefício 4"), "Plano ativo", 35));
        }

        return ResponseEntity.ok(plans);
    }

    private Long extractUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String userIdStr = token.substring(7);
            try {
                return Long.parseLong(userIdStr);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
