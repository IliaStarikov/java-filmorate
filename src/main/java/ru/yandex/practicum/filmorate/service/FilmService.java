package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storage.film_genres.FilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genres.GenresStorage;
import ru.yandex.practicum.filmorate.storage.likes_film.LikesFilmStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate FIRST_FILM = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmDbStorage;
    private final GenresStorage genresDbStorage;
    private final MPAStorage mpaDbStorage;
    private final FilmGenresStorage filmGenresDbStorage;
    private final LikesFilmStorage likesFilmDbStorage;

    public List<Film> getAll() {
        List<Film> films = filmDbStorage.getAll();

        Map<Long, Set<Genre>> mapFilmGenre = filmGenresDbStorage.findGenreOfFilm(films);

        films.forEach(film -> Optional.ofNullable(mapFilmGenre.get(film.getId()))
                .ifPresent(film::addGenre)
        );

        log.info("Запрос на список всех фильмов выполнен.");
        return films;
    }

    public Film create(Film film) {
        isValid(film);

        // поиск жанров в таблице genres
        Set<Genre> genres = getGenresToFilm(film).stream()
                .sorted(Comparator.comparing(Genre::getId, Comparator.naturalOrder()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // поиск MPA в таблице MPA
        MPA mpa = getMPAtoFilm(film);

        // запись film в таблицу с присвоением id
        Film newFilm = filmDbStorage.create(film);

        // запись в таблицу film_genres, изменение объекта film
        filmGenresDbStorage.addGenreToFilm(newFilm, genres);

        newFilm.setMpa(mpa);
        newFilm.addGenre(genres);

        log.info("Фильм успешно добавлен: {}.", newFilm);
        return newFilm;
    }

    public Film update(Film film) {
        isValid(film);
        getFilmById(film.getId());

        filmGenresDbStorage.removeGenreFromFilm(film.getId());
        filmDbStorage.update(film);

        filmGenresDbStorage.addGenreToFilm(film, film.getGenres());

        log.info("Фильм успешно обновлен: {}.", film);
        return film;
    }

    public Film deleteFilm(Long id) {
        Film filmForDelete = getFilmById(id);

        filmDbStorage.deleteFilm(id);

        log.info("Фильм успешно удален: {}.", filmForDelete);
        return filmForDelete;
    }

    public Film getFilmById(Long filmId) {
        Film findFilm = filmDbStorage.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id: " + filmId + " не найден."));

        findFilm.addGenre(filmGenresDbStorage.findGenreOfFilm(List.of(findFilm))
                .getOrDefault(filmId, Set.of())
        );

        log.info("Запрос по поиску фильма обработан. Найден фильм: {}.", findFilm);
        return findFilm;
    }

    public void addFilmLike(Long filmId, Long userId) {
        likesFilmDbStorage.addLikeFilm(filmId, userId);
        log.info("Пользователь с id: {} поставил 'like' фильму с id: {}.", userId, filmId);
    }

    public void deleteFilmLike(Long filmId, Long userId) {
        likesFilmDbStorage.deleteLikeFilm(filmId, userId);
        log.info("Пользователь с id: {} удалил 'like' у фильма с id: {}.", userId, filmId);
    }

    public List<Film> getPopularFilms(Integer count) {
        List<Film> films = filmDbStorage.getAllPopularFilm(count);

        Map<Long, Set<Genre>> mapFilmsGenres = filmGenresDbStorage.findGenreOfFilm(films);

        films.forEach(film -> {
            Set<Genre> genres = mapFilmsGenres.getOrDefault(film.getId(), Set.of());
            film.addGenre(genres);
        });

        log.info("Запрос на получение популярных фильмов обработан.");
        return films;
    }

    private void isValid(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(FIRST_FILM)) {
            throw new ValidationException(String.format("Ошибка валидации, неверная дата релиза %s.", film.getName()));
        }
    }

    private Set<Genre> getGenresToFilm(Film film) {
        Set<Genre> genres = film.getGenres().stream()
                .map(genre -> genresDbStorage.getGenreById(genre.getId())
                        .orElseThrow(() -> new ValidationException("Указан несуществующий жанр.")))
                .collect(Collectors.toSet());

        film.addGenre(genres);
        return genres;
    }

    private MPA getMPAtoFilm(Film film) {
        int mpaID = film.getMpa().getId();
        return mpaDbStorage.getMPAById(mpaID)
                .orElseThrow(() -> new ValidationException("Указан несуществующий MPA рейтинг."));
    }
}
