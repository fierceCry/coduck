package coduck.igochaja.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import coduck.igochaja.Service.UserService;
import coduck.igochaja.Model.User;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/signup")
    public User signUpUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/login")
    public User authenticateUser(@RequestBody User login) {
        return userService.loginUser(login.getEmail(), login.getPassword());
    }
}