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

    // 다른 회원가입 관련 메서드들을 구현할 수 있습니다.
    // ...
}