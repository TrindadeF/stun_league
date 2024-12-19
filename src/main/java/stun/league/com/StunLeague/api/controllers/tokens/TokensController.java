package stun.league.com.StunLeague.api.controllers.tokens;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import stun.league.com.StunLeague.infra.config.security.tokens.JWTService;
import stun.league.com.StunLeague.infra.data.repositories.UserRepository;

@Controller
@RequestMapping("/v1/tokens")
@CrossOrigin("*")
public class TokensController {

    private final JWTService jwtService;
    private final UserRepository userRepository;

    public TokensController(JWTService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
}
