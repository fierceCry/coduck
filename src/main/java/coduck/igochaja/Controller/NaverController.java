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
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

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
    public ResponseEntity<Object> redirectToNaverAuthorization(HttpServletResponse response, HttpSession session) {
        try {
            String stateToken = generateStateToken();
            String naverAuthUrl = buildNaverAuthUrl(stateToken);

            session.setAttribute("stateToken", stateToken);
            response.sendRedirect(naverAuthUrl);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "리디렉션 중 오류가 발생했습니다: " + e.getMessage()));
        }
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

    @ResponseBody
    @GetMapping("/callback")
    public ResponseEntity<Map<String, Object>> handleNaverCallback(@RequestParam("code") String code, @RequestParam(value = "state", required = false) String state, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null || !state.equals(session.getAttribute("stateToken"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Invalid state token"));
        }

        ResponseEntity<String> responseEntity = exchangeCodeForAccessToken(code, state);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Failed to retrieve access token"));
        }

        String accessToken = new JSONObject(responseEntity.getBody()).optString("access_token");
        if(accessToken.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Access token not found"));
        }

        Map<String, Object> userInfo = naverService.getUserInfo(accessToken);
        return ResponseEntity.ok(userInfo);
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
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/naver/oauth2/login";
    }
}