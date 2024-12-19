package stun.league.com.StunLeague.infra.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import stun.league.com.StunLeague.infra.entities.UserSocialMedia;

import java.util.Optional;

@Repository
@Transactional
public interface UsersSocialMediaRepository extends JpaRepository<UserSocialMedia, Long> {

    @Query("SELECT us FROM UserSocialMedia us  WHERE us.user.id =: userId AND us.socialMedia.id =: socialMediaId")
    Optional<UserSocialMedia> findByUserIdAndSocialMediaId(@Param("userId") Long userId, @Param("socialMediaId") Long socialMediaId);
}
