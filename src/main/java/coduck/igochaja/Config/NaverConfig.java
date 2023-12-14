package coduck.igochaja.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class NaverConfig {
    @Value("${naver.oauth.requestTokenUri}")
    private String requestTokenUri;

    @Value("${naver.oauth.clientId}")
    private String clientId;

    @Value("${naver.oauth.clientSecret}")
    private String clientSecret;

    @Value("${naver.oauth.redirectUri}")
    private String redirectUri;

    @Value("${naver.oauth.accessTokenUri}")
    private String accessTokenUri;
}
