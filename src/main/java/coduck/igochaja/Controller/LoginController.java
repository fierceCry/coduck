package coduck.igochaja.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import coduck.igochaja.Model.Login;
import coduck.igochaja.Service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;
    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/authenticate")
    public Login authenticateUser(@RequestBody Login login) {
        return loginService.loginUser(login.getEmail(), login.getPassword());
    }
}
