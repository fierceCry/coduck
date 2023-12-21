package coduck.igochaja.Controller;

import coduck.igochaja.Config.GoogleConfig;
import coduck.igochaja.Model.User;
import coduck.igochaja.Service.GoogleService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> googleLogin(@RequestParam String code) {
        try {
        Map<String, Object> userInfo = googleService.socialLogin(code);
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
