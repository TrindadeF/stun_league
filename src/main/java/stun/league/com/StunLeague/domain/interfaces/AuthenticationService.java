package stun.league.com.StunLeague.domain.interfaces;

import stun.league.com.StunLeague.domain.models.dto.auth.AuthenticationResponseDTO;
import stun.league.com.StunLeague.domain.models.dto.auth.UserRequestLoginDTO;

public interface AuthenticationService {

    AuthenticationResponseDTO login(UserRequestLoginDTO user);

}
