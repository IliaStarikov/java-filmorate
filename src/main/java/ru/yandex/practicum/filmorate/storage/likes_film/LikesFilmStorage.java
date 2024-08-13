package ru.yandex.practicum.filmorate.storage.likes_film;

public interface LikesFilmStorage {
    void addLikeFilm(long filmId, long userId);

    void deleteLikeFilm(long filmId, long userId);
}
