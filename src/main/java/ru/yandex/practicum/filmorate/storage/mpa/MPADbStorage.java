package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Optional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("mpaDbStorage")
@RequiredArgsConstructor
public class MPADbStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<MPA> getMPAById(int id) {
        List<MPA> mpaList = jdbcTemplate.query("SELECT * FROM mpa_rating WHERE id = ?",
                new Object[]{id},
                this::mapRowToMPA);
        return mpaList.stream().findFirst();
    }

    @Override
    public List<MPA> getAllMPA() {
        return jdbcTemplate.query("SELECT * FROM mpa_rating ORDER BY id", this::mapRowToMPA);
    }

    private MPA mapRowToMPA(ResultSet resultSet, int rowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
