package stun.league.com.StunLeague.application.services.auth;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import stun.league.com.StunLeague.domain.exceptions.InvalidCredentailsException;
import stun.league.com.StunLeague.domain.exceptions.NotFoundUserException;
import stun.league.com.StunLeague.domain.interfaces.AuthenticationService;
import stun.league.com.StunLeague.domain.interfaces.services.MatchService;
import stun.league.com.StunLeague.domain.interfaces.services.QueueService;
import stun.league.com.StunLeague.infra.entities.Player;
import stun.league.com.StunLeague.domain.models.dto.auth.AuthenticationResponseDTO;
import stun.league.com.StunLeague.domain.models.dto.auth.PlayerInformationsDTO;
import stun.league.com.StunLeague.domain.models.dto.auth.UserRequestLoginDTO;
import stun.league.com.StunLeague.infra.config.security.tokens.JWTService;
import stun.league.com.StunLeague.infra.data.repositories.PlayerRepository;

import java.util.Date;
import java.util.Optional;


@Service
public class AuthenticationServiceImpl  implements AuthenticationService {

    private final PlayerRepository playerRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MatchService matchService;
    private final QueueService queueService;

    public AuthenticationServiceImpl(PlayerRepository playerRepository, JWTService jwtService, AuthenticationManager authenticationManager, MatchService matchService, QueueService queueService) {
        this.playerRepository = playerRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.matchService = matchService;
        this.queueService = queueService;
    }

    @Override
    public AuthenticationResponseDTO login(UserRequestLoginDTO dto) {
        Player player = this.playerRepository.findByUsername(dto.username()).orElseThrow(() -> new NotFoundUserException("Usuario não existe"));
        var user = player.getUser();
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), dto.password(), user.getAuthorities()));
        } catch (Exception e) {
            throw new InvalidCredentailsException("Senha inválida: " + e.getMessage());
        }
        String token = this.jwtService.generateToken(user);
        Date expirationTokenDate = this.jwtService.extractClaim(token, Claims::getExpiration);
        PlayerInformationsDTO playerInformationsDTO = new PlayerInformationsDTO(
                this.matchService.getMatchIdByPlayerId(player.getId()),
                this.queueService.getQueueIdByPlayerId(player.getId())
        );

        return new AuthenticationResponseDTO(
                user.getId(),
                player.getId(),
                user.getName(),
                user.getPlayer().getUsername(),
                user.getEmail(),
                user.getImageProfile(),
                token, expirationTokenDate,
                playerInformationsDTO);
    }


}
