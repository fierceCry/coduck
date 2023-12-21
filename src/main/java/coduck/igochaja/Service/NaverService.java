package coduck.igochaja.Service;

import coduck.igochaja.Config.JwtTokenConfig;
import coduck.igochaja.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class NaverService {

//    @Autowired
//    private JwtTokenConfig jwtTokenConfig;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public ResponseEntity<?> getUserInfo(String accessToken) {
//        try {
//            String userInfo = requestUserInfo(accessToken);
//            return processUserInfo(userInfo);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("오류가 발생했습니다: " + e.getMessage());
//        }
//    }
//
//    private String requestUserInfo(String accessToken) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + accessToken);
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        String naverApiMeUrl = "https://openapi.naver.com/v1/nid/me";
//
//        ResponseEntity<String> response = restTemplate.exchange(naverApiMeUrl, HttpMethod.GET, entity, String.class);
//        if (response.getStatusCode() != HttpStatus.OK) {
//            throw new RuntimeException("사용자 정보 요청 실패");
//        }
//        return response.getBody();
//    }
//
//    private ResponseEntity<?> processUserInfo(String userInfoJson) {
//        JSONObject userInfo = new JSONObject(userInfoJson);
//        String socialId = userInfo.getJSONObject("response").getString("id");
//        String name = userInfo.getJSONObject("response").getString("name");
//        String email = userInfo.getJSONObject("response").getString("email");
//        String image = userInfo.getJSONObject("response").getString("profile_image");
//        String social = "NAVER";
//
//        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(email, social));
//        if (existingUser.isPresent()) {
//            return ResponseEntity.ok().body(generateTokenMap(existingUser.get()));
//        } else {
//            User newUser = new User(socialId, name, email, social, image);
//            User savedUser = userRepository.save(newUser);
//            return ResponseEntity.ok().body(generateTokenMap(savedUser));
//        }
//    }
//
//    private Map<String, String> generateTokenMap(User user) {
//        String token = jwtTokenConfig.generateToken(user);
//        Map<String, String> tokenMap = new HashMap<>();
//        tokenMap.put("token", token);
//        tokenMap.put("nickname", user.getNickName());
//        tokenMap.put("email", user.getEmail());
//        tokenMap.put("image", user.getImage());
//        return tokenMap;
//    }
}
