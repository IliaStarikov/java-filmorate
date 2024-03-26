package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate FIRST_FILM = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        isValid(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        isValid(film);
        return filmStorage.update(film);
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.getFilm(filmId);
    }

    public void addFilmLike(Long filmId, Long userId) {
        filmStorage.checkFilmExists(filmId);
        userStorage.checkUserExists(userId);

        filmStorage.getFilm(filmId).addLike(userId);
    }

    public void deleteFilmLike(Long filmId, Long userId) {
        filmStorage.checkFilmExists(filmId);
        userStorage.checkUserExists(userId);

        filmStorage.getFilm(filmId).deleteLike(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void isValid(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(FIRST_FILM)) {
            throw new ValidationException(String.format("Ошибка валидации, неверная дата релиза %s", film.getName()));
        }
    }
}
