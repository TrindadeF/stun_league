package stun.league.com.StunLeague.infra.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import stun.league.com.StunLeague.domain.enums.ConfigurationName;

import java.util.List;

@Entity
@Table(name = "configurations")
@Data
@AllArgsConstructor
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ConfigurationName name;

    @OneToMany(mappedBy = "configuration")
    private List<UserConfiguration> userConfigurations;


    public Configuration() {

    }
}
