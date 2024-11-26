package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MessageJdbcTemplateDao {

    private final JdbcTemplate jdbcTemplate;
    public List<Message> findByUserId(int userId) {
        String getMessageQuery = "SELECT * FROM \"message\" WHERE user_id = ?";
        int getMessagesParams = userId;
        return this.jdbcTemplate.queryForStream(
            getMessageQuery,
            (resultSet, rowNum) -> new Message(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("message"),
                resultSet.getTimestamp("created_at")
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            ),
            getMessagesParams
        ).toList();
    }

    public List<Message> save(Integer userId, String message) {
        if (true) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 롤백 여부를 확인 하기 위한 의도된 예외");
        }
        String createMessageQuery = "INSERT INTO \"message\" (user_id, message, created_at) VALUES (?, ?, ?)";
        Object[] createMessageParams = new Object[] {
            userId,
            message,
            LocalDateTime.now()
        };
        this.jdbcTemplate.update(
            createMessageQuery,
            createMessageParams
        );

        String getMessageQuery = "SELECT * FROM \"message\" WHERE user_id = ?";
        int getMessageParams = userId;
        return this.jdbcTemplate.queryForStream(
            getMessageQuery,
            (resultSet, rowNom) -> new Message(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("message"),
                resultSet.getTimestamp("create_at")
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            ),
            getMessageParams
        ).toList();
    }
}
