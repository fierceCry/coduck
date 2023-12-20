package coduck.igochaja.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> saveUser(String socialId, String name, String email, String social, String image) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String sql = "INSERT INTO users (social_id, name, email, social, image) VALUES (?, ?, ?, ?, ?)";

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, socialId);
                ps.setString(2, name);
                ps.setString(3, email);
                ps.setString(4, social);
                ps.setString(5, image);
                return ps;
            }, keyHolder);
            return keyHolder.getKeys();
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error while processing");
            return errorResponse;
        }
    }

    public Map<String, Object> findUserByEmailAndSocial(String email, String social) {
        String sql = "SELECT id, name, email, social, image, social_id FROM users WHERE email = ? AND social = ?";
        List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, email, social);
        return users.isEmpty() ? null : users.get(0);
    }

    public int updateProfileImage(String fileUrl, String userId) {
        try {
            int id = Integer.parseInt(userId);
            String sql = "UPDATE users SET image = ? WHERE id = ?";
            return jdbcTemplate.update(sql, fileUrl, id);
        }catch (Exception e) {
            return 0;
        }
    }

    public Map<String, Object> findUserById(String userId) {
        int id = Integer.parseInt(userId);
        String sql = "SELECT id, name, email, social, image, social_id FROM users WHERE id = ?";
        List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, id);
        return users.isEmpty() ? null : users.get(0);
    }
}