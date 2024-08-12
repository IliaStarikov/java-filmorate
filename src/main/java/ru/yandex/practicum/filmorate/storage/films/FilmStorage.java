package ru.yandex.practicum.filmorate.storage.films;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getAll();

    Film create(final Film film);

    Film update(final Film film);

    Optional<Film> getFilm(Integer filmId);

    Film deleteFilm(Integer id);

    List<Film> getAllPopularFilm(int count);
}
