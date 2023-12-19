package coduck.igochaja.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;

public class GoogleController {
@GetMapping("goole")
    public void fetGoogleAuthUrl(HttpServletResponse response) throws Exception{
    Object googleoauth = null;
    response.sendRedirect(googleoauth.getOauthRedirectURL());
}

}
