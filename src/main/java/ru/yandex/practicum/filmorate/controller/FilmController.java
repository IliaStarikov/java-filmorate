package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

/**
 * Контроллер для {@link Film}
 */
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final LocalDate FIRST_FILM = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();
    private long filmId = 0L;

    @GetMapping
    public List<Film> getAll() {
        log.info("Получен список, кол-во фильмов {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM)) {
            throw new ValidationException("Дата релиза не может быть раньше " + FIRST_FILM);
        }
        film.setId(++filmId);
        films.put(film.getId(), film);
        log.info("Фильм добавлен {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody final Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Ключ не найден: " + film.getId());
        }
        if (film.getReleaseDate().isBefore(FIRST_FILM)) {
            throw new ValidationException("Дата релиза не может быть раньше " + FIRST_FILM);
        }

        films.put(film.getId(), film);
        log.info("Фильм обновлен {}", film);
        return film;
    }
}
