package ru.yandex.practicum.filmorate.storage.film_genres;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;
import java.util.Map;
import java.util.List;

public interface FilmGenresStorage {
    void addGenreToFilm(Film film, Set<Genre> genres);

    Map<Long, Set<Genre>> findGenreOfFilm(List<Film> films);

    Set<Genre> removeGenreFromFilm(Film film, List<Genre> genres);

    void removeGenreFromFilm(long id);
}