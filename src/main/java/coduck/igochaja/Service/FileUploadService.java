package coduck.igochaja.Service;


import coduck.igochaja.Model.User;
import coduck.igochaja.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FileUploadService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Map<String, Object>> uploadFile(String fileUrl, String objectId) {
        User updatedUser = userRepository.ProfileImage(fileUrl, objectId);
        if (updatedUser != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "I successfully modified my profile image");
            response.put("Image", updatedUser.getImage());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Profile image modification failed")); // 실패시 메시지 반환
        }
    }
}