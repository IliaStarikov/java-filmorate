package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

/**
 * Контроллер для {@link Film}
 */
@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {

    FilmService filmService;

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
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

    @PutMapping("{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") Long filmId,
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
            @RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }
}
