package stun.league.com.StunLeague.api.controllers.verifications;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import stun.league.com.StunLeague.infra.data.repositories.UserRepository;

@Controller
@RequestMapping("/v1/verifications")
@CrossOrigin("*")
public class VerifyUsernameController {

    private final UserRepository userRepository;

    public VerifyUsernameController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("{username}")
    public ResponseEntity verifyUsername(@PathVariable String username) {
        return ResponseEntity.ok(this.userRepository.checkIfNameExist(username));
    }

}
