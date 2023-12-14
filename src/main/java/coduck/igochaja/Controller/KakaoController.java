package coduck.igochaja.Controller;

import coduck.igochaja.Model.User;
import coduck.igochaja.Service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/kakao/oauth2")
public class KakaoController {
    private final KakaoService kaKaoService;

    public KakaoController(KakaoService kakaoUserService) {
        this.kaKaoService = kakaoUserService;
    }

    @GetMapping("/callback")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestParam String code){
        try {

        String access_Token = kaKaoService.getAccessToken(code);

        Map<String, Object> userInfo = kaKaoService.getUserInfo(access_Token);
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
