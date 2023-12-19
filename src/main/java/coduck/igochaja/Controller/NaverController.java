package coduck.igochaja.Controller;

import coduck.igochaja.Config.NaverConfig;
import coduck.igochaja.Service.NaverService;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.http.Cookie;

@Controller
@RequestMapping("/naver/oauth2")
public class NaverController {

    private static final String RESPONSE_TYPE = "code";
    private static final String GRANT_TYPE = "authorization_code";
    private final NaverConfig naverConfig;
    private final NaverService naverService;

    @Autowired
    public NaverController(NaverConfig naverConfig, NaverService naverService) {
        this.naverConfig = naverConfig;
        this.naverService = naverService;
    }

    @GetMapping("/login")
    public void redirectToNaverAuthorization(HttpServletResponse response, HttpSession session) throws IOException {
        String stateToken = generateStateToken();
        String naverAuthUrl = buildNaverAuthUrl(stateToken);

        session.setAttribute("stateToken", stateToken);
        response.sendRedirect(naverAuthUrl);
    }

    private String generateStateToken() {
        return UUID.randomUUID().toString();
    }

    private String buildNaverAuthUrl(String stateToken) {
        return UriComponentsBuilder.fromUriString(naverConfig.getRequestTokenUri())
                .queryParam("response_type", RESPONSE_TYPE)
                .queryParam("client_id", naverConfig.getClientId())
                .queryParam("redirect_uri", naverConfig.getRedirectUri())
                .queryParam("state", stateToken)
                .toUriString();
    }

    @GetMapping("/callback")
    @ResponseBody
    public ResponseEntity<?> handleNaverCallback(@RequestParam("code") String code, @RequestParam(value = "state", required = false) String state, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null || !state.equals(session.getAttribute("stateToken"))) {
            return new ResponseEntity<>("Invalid state token", HttpStatus.FORBIDDEN);
        }

        ResponseEntity<String> responseEntity = exchangeCodeForAccessToken(code, state);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>("Failed to retrieve access token", HttpStatus.BAD_REQUEST);
        }

        String accessToken = new JSONObject(responseEntity.getBody()).optString("access_token");
        if(accessToken.isEmpty()){
            return new ResponseEntity<>("Access token not found", HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<?> userInfo = naverService.getUserInfo(accessToken);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    private ResponseEntity<String> exchangeCodeForAccessToken(String code, String state) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", GRANT_TYPE);
        params.add("client_id", naverConfig.getClientId());
        params.add("client_secret", naverConfig.getClientSecret());
        params.add("code", code);
        params.add("state", state);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        return restTemplate.exchange(naverConfig.getAccessTokenUri(), HttpMethod.POST, requestEntity, String.class);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 쿠키 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) { // 인증 관련 쿠키 이름으로 변경
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
        // 캐시 제어 헤더 추가
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        return "redirect:/naver/oauth2/login"; // 로그인 페이지로 리다이렉트
    }
}