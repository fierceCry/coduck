package coduck.igochaja.Service;

import coduck.igochaja.Config.JwtTokenConfig;
import coduck.igochaja.Config.NaverConfig;
import coduck.igochaja.Repository.UserRepository;
import coduck.igochaja.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@Service
public class NaverService {

    private static final Logger logger = LoggerFactory.getLogger(NaverService.class);

    @Autowired
    private JwtTokenConfig jwtTokenConfig;

    @Autowired
    public NaverService(NaverConfig naverProperties) {
    }

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> getUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            RestTemplate restTemplate = new RestTemplate();

            HttpEntity<String> entity = new HttpEntity<>(headers);
            String NAVER_API_ME_URL = "https://openapi.naver.com/v1/nid/me";
            ResponseEntity<String> userInfoResponseEntity = restTemplate.exchange(NAVER_API_ME_URL, HttpMethod.GET, entity, String.class);

            if (userInfoResponseEntity.getStatusCode() == HttpStatus.OK) {
                JSONObject userInfoJson = new JSONObject(userInfoResponseEntity.getBody());
                String socialId = userInfoJson.getJSONObject("response").getString("id");
                String name = userInfoJson.getJSONObject("response").getString("name");
                String email = userInfoJson.getJSONObject("response").getString("email");
                String image = userInfoJson.getJSONObject("response").getString("profile_image");
                String social = "NAVER";
                User savedUser = userRepository.saveUser(socialId, name, email, social, image);
                String token = jwtTokenConfig.generateToken(savedUser);
                Map<String, String> tokenMap = new HashMap<>();
                tokenMap.put("token", token);
                tokenMap.put("nickname", name);
                tokenMap.put("email", email);
                tokenMap.put("image", image);

                return ResponseEntity.ok().body(tokenMap);
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save user's information");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred");
        }
    }
}