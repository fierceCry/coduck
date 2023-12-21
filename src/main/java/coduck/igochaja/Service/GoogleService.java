package coduck.igochaja.Service;

import coduck.igochaja.Config.GoogleConfig;
import coduck.igochaja.Config.JwtTokenConfig;
import coduck.igochaja.Repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleService {

    private final RestTemplate restTemplate = new RestTemplate();

    private GoogleConfig googleConfig;
    private UserRepository userRepository;
    private JwtTokenConfig jwtTokenConfig;

    @Autowired
    public GoogleService(GoogleConfig googleConfig, UserRepository userRepository, JwtTokenConfig jwtTokenConfig) {
        this.userRepository = userRepository;
        this.googleConfig = googleConfig;
        this.jwtTokenConfig = jwtTokenConfig;
    }

    public Map<String, Object> socialLogin(String code) {
        String accessToken = getAccessToken(code);
        JsonNode userInfo = getUserInfo(accessToken);

        return processUserInfo(userInfo);
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

    private Map<String, Object> processUserInfo(JsonNode userInfo) {
        try {
            String socialId = userInfo.get("id").asText();
            String name = userInfo.get("name").asText();
            String email = userInfo.get("email").asText();
            String image = userInfo.path("profile_image").asText();
            String social = "GOOGLE";

            Map<String, Object> existingUser = userRepository.findUserByEmailAndSocial(email, social);

            if (existingUser != null) {
                return generateTokenMap(existingUser);
            } else {
                // 이 부분에서 UserRepository의 saveUser 메서드를 사용하여 유저를 저장
                Map<String, Object> savedUser = userRepository.saveUser(socialId, name, email, social, image);

                if (savedUser != null && !savedUser.isEmpty()) {
                    return generateTokenMap(savedUser);
                } else {
                    return Map.of("message", "user registration failed", "status", HttpStatus.BAD_REQUEST.value());
                }
            }
        } catch (Exception e) {
            return Map.of("message", "JSON parsing failed", "status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private Map<String, Object> generateTokenMap(Map<String, Object> result) {
        String token = jwtTokenConfig.generateToken(result);
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("name", result.get("name"));
        tokenMap.put("email", result.get("email"));
        tokenMap.put("image", result.get("image"));
        return tokenMap;
    }
}
