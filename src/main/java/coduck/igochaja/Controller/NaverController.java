package coduck.igochaja.Controller;

import coduck.igochaja.Config.NaverConfig;
import coduck.igochaja.Service.NaverService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Controller
@RequestMapping("/login/oauth2")
public class NaverController {

    private static final Logger logger = LoggerFactory.getLogger(NaverController.class);

    private static final String RESPONSE_TYPE = "code";
    private static final String GRANT_TYPE = "authorization_code";

    private final NaverConfig naverConfig;
    private final NaverService naverService;

    @Autowired
    public NaverController(NaverConfig naverConfig, NaverService naverService) {
        this.naverConfig = naverConfig;
        this.naverService = naverService;
    }

    @GetMapping("/naver")
    public void redirectToNaverAuthorization(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String stateToken = generateStateToken();
        String naverAuthUrl = buildNaverAuthUrl(stateToken);

        HttpSession session = request.getSession(true);
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

    @GetMapping("/naver/callback")
    @ResponseBody
    public ResponseEntity<?> handleNaverCallback(@RequestParam("code") String code, @RequestParam(value = "state", required = false) String state, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null || !state.equals(session.getAttribute("stateToken"))) {
            return new ResponseEntity<>("Invalid state token", HttpStatus.FORBIDDEN);
        }

        session.removeAttribute("stateToken");
        ResponseEntity<String> responseEntity = exchangeCodeForAccessToken(code, state);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>("Failed to retrieve access token", HttpStatus.BAD_REQUEST);
        }

        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
        String accessToken = jsonObject.getString("access_token");
        naverService.getUserInfo(accessToken);
        return new ResponseEntity<>("User successfully authenticated", HttpStatus.OK);
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
}
