package ru.yandex.practicum.filmorate.storage.genres;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository("genresDbStorage")
@RequiredArgsConstructor
public class GenresDbStorage implements GenresStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres", this::mapRowToGenre);
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM genres WHERE id = ?",
                new Object[]{id},
                this::mapRowToGenre);
        return genres.stream().findFirst();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}