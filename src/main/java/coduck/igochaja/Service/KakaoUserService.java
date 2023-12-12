package coduck.igochaja.Service;

import coduck.igochaja.Model.User;
import coduck.igochaja.Repository.KakaoUserRepository;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


@SuppressWarnings("ALL")
@Slf4j
@Service
public class KakaoUserService extends DefaultOAuth2UserService {
    private static final Logger log = LoggerFactory.getLogger(KakaoUserService.class);

    @Autowired
    KakaoUserRepository kakaoUserRepository;


    public String getAccessToken(String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=I2ZPN4_meOqwrmJVsd-Trrv8iZM-OihnhaaioHM4MulnCQLC4W0rcp_11-AKKiUQAAABjEP9dKRONYg--5I0Sw");
            sb.append("&client_id=22de7cdf05892a7d641b695b045eca61");
            sb.append("&redirect_uri=http://localhost:8080/kakao/callback");
            sb.append("&code=" + authorize_code);
            sb.append("&client_secret=7qcMyQUS1wf9FNbyzstxW9gCT67c1r1I");
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode + "확인");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result + "결과");

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return access_Token;
    }

    public User getuserinfo(String access_Token) {

        HashMap<String, Object> userInfo = new HashMap<String, Object>();

        String requestURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(requestURL); //1.url 객체만들기
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //2.url 에서 url connection 만들기
            conn.setRequestMethod("GET"); // 3.URL 연결구성
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            //키값, 속성 적용
            int responseCode = conn.getResponseCode(); //서버에서 보낸 http 상태코드 반환
            System.out.println("responseCode :" + responseCode+ "여긴가");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // 버퍼를 사용하여 일근ㄴ것
            String line = "";
            String result = "";
            while ((line = buffer.readLine()) != null) {
                result +=line;
            }
            //readLine()) ==> 입력 String 값으로 리턴값 고정

            System.out.println("response body :" +result);

            // 읽엇으니깐 데이터꺼내오기
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result); //Json element 문자열변경
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();
            userInfo.put("nickname", nickname);
            userInfo.put("email", email);


        } catch (Exception e) {
            e.printStackTrace();
        }

//        User result = kakaoUserRepository.findById();
//        System.out.println("S :" + result);
//
//        if(result ==null) {
//            kakaoUserRepository.save(result);
//            return kakaoUserRepository.getKakaoUser(userInfo);
//        }else {
//            return result;
//        }
        User result = new User();
        return result;
    }

}


