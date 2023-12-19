package coduck.igochaja.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Data
@Component
public class GoogleConfig {
    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.secret}")
    private String clientSecret;

    @Value("${google.redirect.url}")
    private String redirectUri;

    @Value("${google.login.url}")
    private String loginUrl;

    @Value("${google.auth.url}")
    private String authUrl;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

}
