package stun.league.com.StunLeague.infra.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "black_keys")
public class BlackKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_key")
    private String idKey;
    private String password;

    public BlackKey() {

    }

}
