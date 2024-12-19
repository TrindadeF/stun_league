package stun.league.com.StunLeague.infra.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "users_configurations")
@Data
@AllArgsConstructor
public class UserConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "configuration_id")
    private Configuration configuration;

    private String value;

    public UserConfiguration() {
    }
}
