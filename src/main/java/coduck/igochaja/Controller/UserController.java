package coduck.igochaja.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import coduck.igochaja.Service.UserService;
import coduck.igochaja.Model.User;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService UserService;

    @Autowired
    public UserController(UserService personService) {
        this.UserService = personService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return UserService.createUser(user);
    }
}

