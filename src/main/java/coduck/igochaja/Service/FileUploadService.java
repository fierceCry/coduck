package coduck.igochaja.Service;


import coduck.igochaja.Model.User;
import coduck.igochaja.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    public ResponseEntity<Map<String, Object>> uploadFile(String fileUrl, String objectId) {
        int rowsAffected = userRepository.updateProfileImage(fileUrl, objectId);
        logger.info("rowsAffected:"+rowsAffected);

        if (rowsAffected > 0) {
            // 성공적으로 업데이트된 경우, 변경된 사용자 정보를 다시 조회
            Map<String, Object> updatedUser = userRepository.findUserById(objectId); // 이 메서드는 구현되어 있어야 합니다.

            logger.info("updatedUser:"+updatedUser);

            if (updatedUser != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "I successfully modified my profile image");
                response.put("Image", updatedUser.get("image")); // 변경된 이미지 URL 포함
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "User not found"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Profile image modification failed")); // 실패시 메시지 반환
        }
    }
}