package stun.league.com.StunLeague.infra.config.security.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Permitir requisições de qualquer origem (por exemplo, o frontend Angular em http://localhost:4200)
        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://5d1e-2804-818c-40e7-e100-f401-bb92-a68d-5558.ngrok-free.app");
        config.addAllowedOrigin("http://localhost:4200");// Adicione outras origens conforme necessário
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
