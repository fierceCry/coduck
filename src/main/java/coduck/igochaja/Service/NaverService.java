package coduck.igochaja.Service;

import coduck.igochaja.Config.JwtTokenConfig;
import coduck.igochaja.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

@Service
public class NaverService {

    @Autowired
    private JwtTokenConfig jwtTokenConfig;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getUserInfo(String accessToken) {
        try {
            String userInfo = requestUserInfo(accessToken);
            return processUserInfo(userInfo);
        } catch (Exception e) {
            Map<String, Object> Response = new HashMap<>();
            Response.put("message", "오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response).getBody();
        }
    }

    private String requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String naverApiMeUrl = "https://openapi.naver.com/v1/nid/me";

        ResponseEntity<String> response = restTemplate.exchange(naverApiMeUrl, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자를 정보조회 오류").getBody();
        }
        return response.getBody();
    }

    private Map<String, Object> processUserInfo(String userInfo) {
        JSONObject userInfoJson = new JSONObject(userInfo);
        String socialId = userInfoJson.getJSONObject("response").getString("id");
        String name = userInfoJson.getJSONObject("response").getString("name");
        String email = userInfoJson.getJSONObject("response").getString("email");
        String image = userInfoJson.getJSONObject("response").getString("profile_image");
        String social = "NAVER";

        Map<String, Object> existingUser = userRepository.findUserByEmailAndSocial(email, social);
        if (existingUser != null) {
            return generateTokenMap(existingUser);
        } else {
            Map<String, Object> savedUser = userRepository.saveUser(socialId, name, email, social, image);
            if (savedUser != null && !savedUser.isEmpty()) {
                return generateTokenMap(savedUser);
            } else {
                return Map.of("message", "user registration failed", "status", HttpStatus.BAD_REQUEST.value());
            }
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
