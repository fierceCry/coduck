package coduck.igochaja.Service;

import coduck.igochaja.Config.JwtTokenConfig;
import coduck.igochaja.Config.KakaoConfig;
import coduck.igochaja.Model.User;
import coduck.igochaja.Repository.UserRepository;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Service
public class KakaoService extends DefaultOAuth2UserService {
    private static final Logger log = LoggerFactory.getLogger(KakaoService.class);

    private UserRepository userRepository;

    private KakaoConfig kakaoConfig;

    private JwtTokenConfig jwtTokenConfig;

    @Autowired
    public KakaoService(UserRepository userRepository, KakaoConfig kakaoConfig, JwtTokenConfig jwtTokenConfig) {
        this.userRepository = userRepository;
        this.kakaoConfig = kakaoConfig;
        this.jwtTokenConfig = jwtTokenConfig;
    }

    public String getAccessToken(String code) {
        String accessToken = "";
        String refreshToken = "";

        try{
            URL url = new URL(kakaoConfig.getTokenUri());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(kakaoConfig.getClientId());
            sb.append("&redirect_uri=").append(kakaoConfig.getRedirectUri());
            sb.append("&client_secret=").append(kakaoConfig.getClientSecret());
            sb.append("&code=").append(code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();
            log.info("kakaoservice.result :::" + result);
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        }catch (Exception e){
            log.error("An error occurred while getting user info from KakaoService/getAccessToken: ", e);
        }
        log.error("getAccessToken successfully.");
        return accessToken;
    }

    public HashMap<String, Object> getUserInfo(String accessToken) {
        HashMap<String, Object> tokenMap = new HashMap<>();
        try{
            URL url = new URL(kakaoConfig.getUserInfoUri());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            int responseCode = conn.getResponseCode();

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }

            String result = responseSb.toString();

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            JsonObject profile = kakaoAccount.get("profile").getAsJsonObject();
            String socialId = element.getAsJsonObject().get("id").getAsString();
            String name = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
            String image = profile.getAsJsonObject().get("profile_image_url").getAsString();
            String social = "KAKAO";

            User resultUser = new User(socialId, name, email, social, image);

            if(!Optional.ofNullable(userRepository.findByEmail(email, social)).isPresent()){
                resultUser = userRepository.saveUser(socialId, name, email, social, image);
            }

            String token = jwtTokenConfig.generateToken(resultUser);

            tokenMap.put("token", token);
            tokenMap.put("nickname", name);
            tokenMap.put("email", email);
            tokenMap.put("image", image);

            br.close();

        }catch (Exception e){
            log.error("An error occurred while getting user info from KakaoService/getUserInfo: ", e);
            tokenMap.put("error", "An error occurred while getting user info from Kakao login.");
        }
        tokenMap.put("success", "User information retrieved successfully.");
        return tokenMap;
    }
}


