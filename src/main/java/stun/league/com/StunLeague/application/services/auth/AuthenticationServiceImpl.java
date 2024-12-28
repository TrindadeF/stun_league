package stun.league.com.StunLeague.application.services.auth;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stun.league.com.StunLeague.domain.exceptions.InvalidCredentailsException;
import stun.league.com.StunLeague.domain.exceptions.NotFoundUserException;
import stun.league.com.StunLeague.domain.interfaces.AuthenticationService;
import stun.league.com.StunLeague.domain.interfaces.services.MatchService;
import stun.league.com.StunLeague.domain.interfaces.services.QueueService;
import stun.league.com.StunLeague.domain.models.dto.auth.AuthenticationResponseDTO;
import stun.league.com.StunLeague.domain.models.dto.auth.PlayerInformationsDTO;
import stun.league.com.StunLeague.domain.models.dto.auth.UserRequestLoginDTO;
import stun.league.com.StunLeague.domain.models.dto.auth.UserRequestRegisterDTO;
import stun.league.com.StunLeague.infra.config.security.tokens.JWTService;
import stun.league.com.StunLeague.infra.data.repositories.PlayerRepository;
import stun.league.com.StunLeague.infra.data.repositories.UserRepository;
import stun.league.com.StunLeague.infra.entities.Player;
import stun.league.com.StunLeague.infra.entities.User;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final MatchService matchService;
    private final QueueService queueService;

    public AuthenticationServiceImpl(PlayerRepository playerRepository,
                                     UserRepository userRepository,
                                     JWTService jwtService,
                                     AuthenticationManager authenticationManager,
                                     PasswordEncoder passwordEncoder,
                                     MatchService matchService,
                                     QueueService queueService) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.matchService = matchService;
        this.queueService = queueService;
    }

    @Override
    public AuthenticationResponseDTO login(UserRequestLoginDTO dto) {
        Player player = this.playerRepository.findByUsernameOrEmail(dto.usernameOrEmail())
                .orElseThrow(() -> new NotFoundUserException("Usuário não encontrado"));

        User user = player.getUser();

        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.usernameOrEmail(), dto.password(), user.getAuthorities())
            );
        } catch (Exception e) {
            throw new InvalidCredentailsException("Credenciais inválidas: " + e.getMessage());
        }

        // Gerar o token JWT
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
                player.getUsername(),
                user.getEmail(),
                user.getImageProfile(),
                token,
                expirationTokenDate,
                playerInformationsDTO
        );
    }

    @Override
    public AuthenticationResponseDTO register(UserRequestRegisterDTO registerDTO) {
        // Verificação de email existente
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso!");
        }

        // Verificação de username existente
        if (playerRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Nome de usuário já está em uso!");
        }

        // Criar o usuário
        User user = new User();
        user.setName(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        userRepository.save(user);

        // Criar o player vinculado ao usuário
        Player player = new Player();
        player.setUser(user);
        player.setUsername(registerDTO.getUsername());
        player.setPoints(BigDecimal.ZERO);
        player.setWins(0);
        player.setLosses(0);
        playerRepository.save(player);

        // Gerar o token JWT
        String token = jwtService.generateToken(user);
        Date expirationTokenDate = jwtService.extractClaim(token, Claims::getExpiration);

        return new AuthenticationResponseDTO(
                user.getId(),
                player.getId(),
                user.getName(),
                player.getUsername(),
                user.getEmail(),
                user.getImageProfile(),
                token,
                expirationTokenDate,
                null
        );
    }
}
