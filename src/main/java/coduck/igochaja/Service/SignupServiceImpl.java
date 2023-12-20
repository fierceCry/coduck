package coduck.igochaja.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import coduck.igochaja.Model.Signup;
import coduck.igochaja.Repository.SignupRepository;

@Service
public class SignupServiceImpl implements SignupService {

    private final SignupRepository signupRepository;

    @Autowired
    public SignupServiceImpl(SignupRepository signupRepository) {
        this.signupRepository = signupRepository;
    }

    @Override
    public Signup registerUser(Signup signup) {
        return signupRepository.save(signup);
    }

}