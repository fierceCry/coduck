//package coduck.igochaja.Service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import coduck.igochaja.Model.Login;
//import coduck.igochaja.Repository.LoginRepository;
//
//@Service
//public class LoginServiceImpl implements LoginService {
//
//    private final LoginRepository loginRepository;
//
//    @Autowired
//    public LoginServiceImpl(LoginRepository loginRepository) {
//        this.loginRepository = loginRepository;
//    }
//
//    @Override
//    public Login loginUser(String email, String password) {
//
//        return loginRepository.findByEmailAndPassword(email, password);
//    }
//}
