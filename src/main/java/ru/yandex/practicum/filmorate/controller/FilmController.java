package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.*;

/**
 * Контроллер для {@link Film}
 */
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> getAll() {
        List<Film> films = filmService.getAll();
        return films;
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody final Film film) {
        return filmService.update(film);
    }

    @GetMapping("{id}")
    public Film getFilmById(@PathVariable("id") Long filmId) {
        return filmService.getFilmById(filmId);
    }

    @DeleteMapping("/{id}")
    public Film deleteFilmByID(@PathVariable("id") Long filmId) {
        return filmService.deleteFilm(filmId);
    }

    @PutMapping("{id}/like/{userId}")
    public void putLikeFilm(@PathVariable("id") Long filmId,
                            @PathVariable("userId") Long userId) {
        filmService.addFilmLike(filmId, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteFilmLike(@PathVariable("id") Long filmId,
                               @PathVariable("userId") Long userId) {
        filmService.deleteFilmLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10") @Positive int count) {
        return filmService.getPopularFilms(count);
    }
}
