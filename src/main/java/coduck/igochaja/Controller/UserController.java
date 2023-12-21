package coduck.igochaja.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import coduck.igochaja.Service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService UserService;

    @Autowired
    public UserController(UserService personService) {
        this.UserService = personService;
    }

    @GetMapping("/profile/another")
    public Map<String, Object> getUserByEmail(@RequestParam("email") String email, @RequestParam("social") String social) {
        return UserService.getUserByEmail(email, social);
    }

}