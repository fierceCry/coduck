//package coduck.igochaja.Controller;
//
//
//import coduck.igochaja.Service.KakaoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//import java.util.Map;
//
//
//@RestController
//@RequestMapping("/kakao/oauth2")
//public class KakaoController {
//    private final KakaoService kaKaoService;
//
//    @Autowired
//    public KakaoController(KakaoService kakaoUserService) {
//        this.kaKaoService = kakaoUserService;
//    }
//
//    @GetMapping("/callback")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestParam String code){
//        try {
//
//        String access_Token = kaKaoService.getAccessToken(code);
//
//        Map<String, Object> userInfo = kaKaoService.getUserInfo(access_Token);
//            return new ResponseEntity<>(userInfo, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//}
