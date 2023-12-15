package coduck.igochaja.Config;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import coduck.igochaja.Model.User;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenConfig {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenConfig.class);

    @Value("${coduck.jwt.secret}")
    private String jwtSecret;

    public String generateToken(User user) {
        long now = System.currentTimeMillis();
        long expirationTime = now + 7200000; // 2시간 후 만료

        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expirationTime))
                .claim("socialId", user.getSocialId())
                .claim("social", user.getSocial())
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("JWT Token validation error", e);
            return false;
        }
    }

    public String getSocialId(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.get("_id", String.class);
    }

    public String extractToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}