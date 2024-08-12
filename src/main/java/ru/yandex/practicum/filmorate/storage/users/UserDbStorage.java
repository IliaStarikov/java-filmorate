package ru.yandex.practicum.filmorate.storage.users;

import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.model.User;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Objects;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY id", this::mapRowToUser);
    }

    @Override
    public User create(User user) {
        log.info("Поступил пользователь на добавление {} ", user);

        String sqlQuery = "INSERT INTO users(name, login, email, birthday) " +
                "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        return user.toBuilder().id(id).build();
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users " +
                "SET name = ?, login = ?, email = ?, birthday = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(),
                user.getBirthday(), user.getId());

        return user;
    }

    @Override
    public Optional<User> getUser(Integer id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id = ?",
                new Object[]{id},
                this::mapRowToUser);
        return users.stream().findFirst();
    }

    @Override
    public User deleteUser(Integer id) {
        User user = getUser(id).get();
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
        return user;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
