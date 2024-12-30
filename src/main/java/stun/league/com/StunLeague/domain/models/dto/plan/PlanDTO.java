package stun.league.com.StunLeague.domain.models.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO {
    private String name;
    private List<String> benefits;
    private String description;
    private double price;
}
