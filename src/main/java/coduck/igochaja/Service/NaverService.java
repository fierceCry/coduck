package coduck.igochaja.Service;

import coduck.igochaja.Config.NaverConfig;
import coduck.igochaja.Repository.UserRepository;
import coduck.igochaja.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
import coduck.igochaja.Controller.NaverController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NaverService {

    private static final Logger logger = LoggerFactory.getLogger(NaverController.class);

    @Autowired
    public NaverService(NaverConfig naverProperties) {
    }

    @Autowired
    private UserRepository userRepository;

    public String getUserInfo(String accessToken) {
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
                String id = userInfoJson.getJSONObject("response").getString("id");
                String name = userInfoJson.getJSONObject("response").getString("name");
                String email = userInfoJson.getJSONObject("response").getString("email");
                String social = "NAVER";

                userRepository.saveUser(id, name, email, social);
            } else {
//                ErrorResponse errorResponse = new ErrorResponse("Error message", 404); // or appropriate status code
//                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return "HTTP 요청 오류 발생";
        }
        return accessToken;
    }
}
