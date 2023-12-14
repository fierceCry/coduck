package coduck.igochaja.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import coduck.igochaja.Service.UserService;
import coduck.igochaja.Model.User;

@RestController
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    @Autowired
    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User signUpUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}

