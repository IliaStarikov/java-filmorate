package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    public List<Film> getAll();
    public Film create(final Film film);
    public Film update(final Film film);
    Film getFilm(Long filmId);
    void checkFilmExists(Long filmId);
}
