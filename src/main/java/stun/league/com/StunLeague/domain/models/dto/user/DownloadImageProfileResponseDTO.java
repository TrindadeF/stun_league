package stun.league.com.StunLeague.domain.models.dto.user;

import org.springframework.http.MediaType;

public record DownloadImageProfileResponseDTO(MediaType mediaType, byte[] fileContent) {
}
