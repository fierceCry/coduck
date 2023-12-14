package coduck.igochaja.Service;

import coduck.igochaja.Model.Login;

public interface LoginService {
    Login loginUser(String email, String password);
}