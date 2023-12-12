package coduck.igochaja.Controller;

import coduck.igochaja.Model.KakaoUser;
import coduck.igochaja.Model.User;
import coduck.igochaja.Service.KakaoUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kakao")
public class KakaoController {
    private final KakaoUserService kaKaoService;

    public KakaoController(KakaoUserService kakaoUserService) {
        this.kaKaoService = kakaoUserService;
    }

    @PostMapping
    public String kakaoLogin(@RequestParam(value = "code", required = false) String code, HttpSession session) {
        System.out.println("####### " + code);

        String access_Token = kaKaoService.getAccessToken(code);
        User userInfo = kaKaoService.getuserinfo(access_Token);


        System.out.println("###access_Token#### : " + access_Token);
//        System.out.println("###nickname#### : " + userInfo.getK_name());
//        System.out.println("###email#### : " + userInfo.getK_email());

        return "login success";
    }
}
