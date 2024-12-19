package stun.league.com.StunLeague.infra.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "users_social_media")
@Data
@AllArgsConstructor
public class UserSocialMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "media_id")
    private SocialMedia socialMedia;

    private String value;

    public UserSocialMedia() {
    }
}
