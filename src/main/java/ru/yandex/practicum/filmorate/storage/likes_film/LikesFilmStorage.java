package ru.yandex.practicum.filmorate.storage.likes_film;

public interface LikesFilmStorage {
    void addLikeFilm(int filmId, int userId);

    void deleteLikeFilm(int filmId, int userId);
}
