package coduck.igochaja.Controller;

import coduck.igochaja.Config.GoogleConfig;
import coduck.igochaja.Model.User;
import coduck.igochaja.Service.GoogleService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RestController
@RequestMapping(value = "/google/login", produces = "application/json")
public class GoogleController {
    GoogleService googleService;

    @Autowired
    public GoogleController(GoogleService googleService) {
        this.googleService = googleService;
    }
    @GetMapping("/code")
    public void googleLogin(@RequestParam String code) {
        googleService.socialLogin(code);

    }

}
