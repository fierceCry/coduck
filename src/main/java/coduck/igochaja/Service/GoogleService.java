package coduck.igochaja.Service;

import coduck.igochaja.Config.GoogleConfig;
import coduck.igochaja.Config.JwtTokenConfig;
import coduck.igochaja.Repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleService {

    private final RestTemplate restTemplate = new RestTemplate();

    private GoogleConfig googleConfig;
    private UserRepository userRepository;
    private JwtTokenConfig jwtTokenConfig;

    @Autowired
    public GoogleService(GoogleConfig googleConfig, UserRepository userRepository, JwtTokenConfig jwtTokenConfig)
    {
        this.userRepository = userRepository;
        this.googleConfig = googleConfig;
        this.jwtTokenConfig = jwtTokenConfig;
    }
    public void socialLogin(String code) {
        String accessToken = getAccessToken(code);
        JsonNode userResourceNode = getUserInfo(accessToken);
        System.out.println("userResourceNode = " + userResourceNode);

        String id = userResourceNode.get("id").asText();
        String email = userResourceNode.get("email").asText();
        String nickname = userResourceNode.get("name").asText();
        System.out.println("id = " + id);
        System.out.println("email = " + email);
        System.out.println("nickname = " + nickname);

    }

    private String getAccessToken(String authorizationCode) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", googleConfig.getClientId());
        params.add("client_secret", googleConfig.getClientSecret());
        params.add("redirect_uri", googleConfig.getRedirectUri());
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(googleConfig.getTokenUrl(), HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }
    private JsonNode getUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(googleConfig.getResourceUrl(), HttpMethod.GET, entity, JsonNode.class).getBody();
    }


}

