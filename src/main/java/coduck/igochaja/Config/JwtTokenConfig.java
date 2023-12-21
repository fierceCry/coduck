package coduck.igochaja.Config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenConfig {

    @Value("${coduck.jwt.secret}")
    private String jwtSecret;

    public String generateToken(Map<String, Object> result) {

        long now = System.currentTimeMillis();
        long expirationTime = now + 18000000;

        return Jwts.builder()
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expirationTime))
                .claim("id", result.get("id")) // 또는 적절한 키
                .claim("socialId", result.get("social_id"))
                .claim("social", result.get("social"))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getSocialId(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        int id = (int) claims.get("id");
        return Integer.toString(id);
    }

    public String extractToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}