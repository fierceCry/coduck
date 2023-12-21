package coduck.igochaja.Controller;

import coduck.igochaja.Service.FileUploadService;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import coduck.igochaja.Config.JwtTokenConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FileUploadController {

    private final AmazonS3Client amazonS3Client;
    private final JwtTokenConfig jwtTokenConfig;
    private final FileUploadService fileUploadService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @SneakyThrows
    @PatchMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "No file uploaded"));
        }

        String token = extractToken(request);
        if (token == null || !isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid token"));
        }
        String objectId = getObjectIdFromToken(token);
        if(objectId.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Failed to extract from token"));
        }
        String fileUrl = uploadToS3(file, objectId);
        if(fileUrl.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Upload failed"));
        }
        return fileUploadService.uploadFile(fileUrl, objectId);
    }

    private String extractToken(HttpServletRequest request) {
        return jwtTokenConfig.extractToken(request);
    }

    private boolean isTokenValid(String token) {
        return jwtTokenConfig.validateToken(token);
    }

    private String getObjectIdFromToken(String token) {
        return jwtTokenConfig.getSocialId(token);
    }

    private String uploadToS3(MultipartFile file, String objectId) throws IOException {
        String fileName = objectId + "/" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    @GetMapping("/check/email")
    public @ResponseBody Map<String, Object> mail_find(String userEmail, String userName) {
        Map<String, Object> response = new HashMap<>();
        if (userEmail.isEmpty() || userName.isEmpty()) {
            response.put("message", "userEmail and userName must not be empty");
            return response;
        }
        ResponseEntity<Map<String, Object>> pwFindCheck = fileUploadService.userEmailCheck(userEmail, userName);
        return pwFindCheck.getBody();
    }
}