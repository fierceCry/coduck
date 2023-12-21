package coduck.igochaja.Service;

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
        int rowsAffected = userRepository.updateProfileImage(fileUrl, objectId);

        if (rowsAffected > 0) {
            Map<String, Object> updatedUser = userRepository.findUserById(objectId);

            if (updatedUser != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "I successfully modified my profile image");
                response.put("Image", updatedUser.get("image"));
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "User not found"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Profile image modification failed"));
        }
    }

    public ResponseEntity<Map<String, Object>> userEmailCheck(String userEamil, String userName){
        Map<String, Object> emailCheck = userRepository.EamilCheck(userEamil, userName);
        return ResponseEntity.ok(emailCheck);
    }
}