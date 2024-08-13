package ru.yandex.practicum.filmorate.storage.likes_film;

import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository("LikesFilmDbStorage")
@RequiredArgsConstructor
public class LikesFilmDbStorage implements LikesFilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLikeFilm(long filmId, long userId) {
        String sqlQueryForLikeMark = "INSERT INTO likes_film (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(sqlQueryForLikeMark, filmId, userId);
    }

    @Override
    public void deleteLikeFilm(long filmId, long userId) {
        String sqlQueryForUnLikeMark = "DELETE FROM likes_film WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlQueryForUnLikeMark, filmId, userId);
    }
}