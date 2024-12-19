package stun.league.com.StunLeague.domain.interfaces.facade;

import stun.league.com.StunLeague.domain.models.dto.user.RequestUpdateUserInformationDTO;

public interface UserUpdateInformationFacade {

    void validate(RequestUpdateUserInformationDTO requestUpdateUserInformationDTO);
}
