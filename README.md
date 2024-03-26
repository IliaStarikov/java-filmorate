# java-filmorate
Template repository for Filmorate project.
</br>
</br>
</br>
</br>
</br>
</br>
![BD scheme for java-filmorate](src/main/resources/FilmrateBD.png)
# Описание схемы:
<!-- TOC -->
1. films:
<!-- TOC -->
* film_id - PK идентификатор фильма
* mpa_id FK (ссылает к таблице mpa_rating) - идентификатор рейтинга
* name - название фильма
* description - описание фильма
* release_date - дата релиза
* duration - продолжительность фильма
<!-- TOC -->
2. mpa_rating:
<!-- TOC -->
* mpa_id - PK идентификатор рейтинга
* name - наименование рейтинга
<!-- TOC -->
3. films_genres:
<!-- TOC -->
* film_id -PK (ссылается на таблицу films) идентификатор фильма
* genres_id - PK (ссылается на таблицу genres) идентификатор пользователя
<!-- TOC -->
4. genres:
<!-- TOC -->
* genre_id - PK идентификатор жанра
* name - название жанра
<!-- TOC -->
5. likes_films:
<!-- TOC -->
* film_id - PK (ссылается на таблицу films) идентификатор фильма
* user_id - PK (ссылается на таблицу users) идентификатор пользователя
<!-- TOC -->
6. users:
<!-- TOC -->
* user_id - PK идентификатор пользователя
* email - электронная почта пользователя
* name - имя пользователя
* login - логин пользователя
* birthday - дата рождения пользователя
<!-- TOC -->
7. friendship:
<!-- TOC -->
* user_id - PK (ссылается на таблицу user) идентификатор пользователя
* friend_id (ссылается на таблицу user) идентификатор друга
* status - состояние статуса добавления в друзья (true/false)
<!-- TOC -->
# Примеры запросов к БД:
<!-- TOC -->
1. Получение фильма по идентификатору
<!-- TOC -->
SELECT * </br>
FROM films</br>
WHERE film_id = ?;
<!-- TOC -->
2. Получение списка популярных фильмов
<!-- TOC -->
SELECT f.*</br>
FROM likes_film AS lf</br>
INNER JOIN film AS f ON f.film_id = lf.film_id</br>
GROUP BY f.film_id</br>
ORDER BY COUNT(lf.user_id) DESC</br>
LIMIT ?;
<!-- TOC -->
3. Получение пользователя по идентификатору
<!-- TOC -->
SELECT * </br>
FROM users</br>
WHERE user_id = ?
<!-- TOC -->
4. Получение списка друзей конкретного пользователя
<!-- TOC -->
SELECT u.*</br>
FROM friendship AS fr</br>
INNER JOIN users AS u ON fr.friend_id = u.user_id</br>
WHERE fr.user_id = ? </br>
AND fr.status =  true;
<!-- TOC -->
5. Получение списка общих друзей
<!-- TOC -->
SELECT u.*</br>
FROM users AS u</br>
LEFT JOIN friendship AS s1 ON u.user_id = s1.friend_id</br>
LEFT JOIN friendship AS s2 ON u.user_id = s2.friend_id</br>
WHERE s1.user_id = ? --(user_id)</br>
AND s2.user_id = ? --(friend_id)</br>
AND s1.status = true</br>
AND s2.status = true;
