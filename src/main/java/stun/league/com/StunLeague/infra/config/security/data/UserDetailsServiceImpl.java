package stun.league.com.StunLeague.infra.config.security.data;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import stun.league.com.StunLeague.infra.data.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         if (email.contains("@")) {
             return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        }
        return userRepository.findByName(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}