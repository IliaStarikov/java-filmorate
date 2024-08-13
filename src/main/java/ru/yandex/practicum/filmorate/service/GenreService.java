package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.storage.genres.GenresStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final GenresStorage genresDbStorage;

    public Genre getGenreById(int id) {
        Genre genre = genresDbStorage.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id: " + id + " не найден."));

        log.info("Запрос по поиску жанра с id: {} обработан. Найден жанр: {}.", id, genre);
        return genre;
    }

    public List<Genre> getAllGenre() {
        List<Genre> genres = genresDbStorage.getAll();

        log.info("Запрос на получение списка всех жанров обработан. Найдены жанры: {}.", genres);
        return genres;
    }
}