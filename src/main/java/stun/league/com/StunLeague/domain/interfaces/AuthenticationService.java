package stun.league.com.StunLeague.domain.interfaces;

import stun.league.com.StunLeague.domain.models.dto.auth.AuthenticationResponseDTO;
import stun.league.com.StunLeague.domain.models.dto.auth.UserRequestLoginDTO;
import stun.league.com.StunLeague.domain.models.dto.auth.UserRequestRegisterDTO;


public interface AuthenticationService {

    AuthenticationResponseDTO login(UserRequestLoginDTO user);
    AuthenticationResponseDTO register(UserRequestRegisterDTO registerDTO);

}
