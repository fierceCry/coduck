package coduck.igochaja.Repository;

import coduck.igochaja.Model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findByEmail(String email, String social) {
        try {
            String sqlQuery = "SELECT * FROM users WHERE email = ? AND social = ?";
            return jdbcTemplate.queryForObject(sqlQuery, new Object[]{email, social}, new BeanPropertyRowMapper<>(User.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User createUser(User user) {
        String insertQuery = "INSERT INTO users (social_id, email, password, nick_name, social, report, image) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                             "RETURNING id, social_id as socialId, email, nick_name as nickName, social, report, image, created_at as createdAt, updated_at as updatedAt";
        return jdbcTemplate.queryForObject(
                insertQuery,
                new Object[]{user.getSocialId(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getNickName(),
                            user.getSocial(),
                            user.getReport(),
                            user.getImage()
                },
                new BeanPropertyRowMapper<>(User.class)
        );
    }
}
