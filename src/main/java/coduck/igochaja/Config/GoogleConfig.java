package coduck.igochaja.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

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

    @Value("${google.token-uri}")
    private String tokenUrl;

    @Value("${google.resource-uri}")
    private String resourceUrl;

  // private final ObjectMapper objectMapper;
  // private final RestTemplate restTemplate;

}
