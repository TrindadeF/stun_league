package stun.league.com.StunLeague.infra.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import stun.league.com.StunLeague.domain.enums.SocialMediaName;

import java.util.List;

@Entity
@Table(name = "social_media_type")
@Data
@AllArgsConstructor
public class SocialMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SocialMediaName name;


    @OneToMany(mappedBy = "socialMedia")
    private List<UserSocialMedia> userSocialMedias;


    public SocialMedia() {

    }


}
