package coduck.igochaja.Controller;

import coduck.igochaja.Service.FileUploadService;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import coduck.igochaja.Config.JwtTokenConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Service
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final AmazonS3Client amazonS3Client;
    private final JwtTokenConfig jwtTokenConfig;
    private final FileUploadService fileUploadService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PatchMapping
    public ResponseEntity<Map<String, Object>> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        try {
<<<<<<< HEAD
            String token = jwtTokenConfig.extractToken(request);
            if (token == null || !jwtTokenConfig.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            String socialId = jwtTokenConfig.getSocialId(token);
=======
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "No file uploaded"));
            }

            String token = extractToken(request);
            if (token == null || !isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid token"));
            }
>>>>>>> main

            String objectId = getObjectIdFromToken(token);
            String fileUrl = uploadToS3(file, objectId);

            return fileUploadService.uploadFile(fileUrl, objectId);
        } catch (IOException e) {
            logger.error("File upload failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Unable to save file"));
        }
    }

// HTTP 요청으로부터 토큰 추출
    private String extractToken(HttpServletRequest request) {
        return jwtTokenConfig.extractToken(request);
    }

// 토큰의 유효성 검사
    private boolean isTokenValid(String token) {
        return jwtTokenConfig.validateToken(token);
    }

// 토큰으로부터 ObjectId 추출
    private String getObjectIdFromToken(String token) {
        return jwtTokenConfig.getSocialId(token);
    }

// MultipartFile을 Amazon S3에 업로드하고 파일 URL 반환
    private String uploadToS3(MultipartFile file, String objectId) throws IOException {
        String fileName = objectId + "/" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
}
