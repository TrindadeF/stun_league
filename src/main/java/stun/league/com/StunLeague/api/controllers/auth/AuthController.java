package stun.league.com.StunLeague.api.controllers.auth;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import stun.league.com.StunLeague.domain.interfaces.AuthenticationService;
import stun.league.com.StunLeague.domain.models.dto.auth.AuthenticationResponseDTO;
import stun.league.com.StunLeague.domain.models.dto.auth.UserRequestLoginDTO;

@Controller
@RequestMapping("/v1/authenticate")
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody UserRequestLoginDTO loginDTO) {
        return ResponseEntity.ok(this.authenticationService.login(loginDTO));
    }
}