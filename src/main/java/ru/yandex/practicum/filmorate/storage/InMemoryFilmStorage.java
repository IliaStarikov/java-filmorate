package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long filmId = 0L;

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        film.setId(++filmId);
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен.", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
        checkFilmExists(film.getId());
        films.put(film.getId(), film);
        log.info("Фильм {} обновлен.", film.getName());
        return film;
    }

    @Override
    public Film getFilm(Long filmId) {
        checkFilmExists(filmId);
        return films.get(filmId);
    }

    public void checkFilmExists(Long filmId) throws NotFoundException {
        if (!films.containsKey(filmId)) {
            log.warn("Фильм с ID: {} не найден.", filmId);
            throw new NotFoundException(String.format("Фильм с ID: %s не найден.", filmId));
        }
    }
}
