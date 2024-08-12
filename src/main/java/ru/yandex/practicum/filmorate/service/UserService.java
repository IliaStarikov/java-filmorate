package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserStorage userDbStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    public List<User> getAll() {
        List<User> usersList = userDbStorage.getAll();
        log.info("Запрос на получение всех пользователей: {}, выполнен.", usersList);
        return usersList;
    }

    public User create(User user) {
        log.info("{}", user);
        checkEmail(user);
        setNameIfNotPresent(user);
        log.info("{}", user);
        User newUser = userDbStorage.create(user);
        log.info("Добавлен новый пользователь: {}.", newUser);
        return newUser;
    }

    public User update(User user) {
        User oldUser = getUserById(user.getId());

        if (!oldUser.getEmail().equals(user.getEmail())) {
            checkEmail(user);
        }

        log.info("Пользователь обновлен: {}.", user);
        return userDbStorage.update(user);
    }

    public User getUserById(Integer id) {
        User user = userDbStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден."));
        log.info("Пользователь найден: {}.", user);
        return user;
    }

    public User deleteUser(Integer id) {
        User user = getUserById(id);
        log.info("Пользователь удален: {}.", user);
        return userDbStorage.deleteUser(id);
    }

    public void addFriend(Integer userId, Integer friendId) {
        getUserById(userId);
        getUserById(friendId);
        friendshipDbStorage.addFriend(userId, friendId);
        log.info("User № {} отправил запрос в друзья User № {}.", userId, friendId);
    }

    public void endFriendship(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        friendshipDbStorage.deleteFriend(userId, friendId);
        log.info("Пользователи {} и {} больше не друзья.", user, friend);
    }

    public List<User> getFriends(Integer userId) {
        getUserById(userId);
        List<User> friends = friendshipDbStorage.getUserFriends(userId);
        log.info("Запрос на получение списка друзей пользователя с id: {} выполнен. Список друзей: {}.",
                userId, friends);
        return friends;
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        getUserById(userId);
        getUserById(friendId);

        List<User> commonFriends = friendshipDbStorage.getCommonFriends(userId, friendId);
        log.info("Запрос на получение общих друзей пользователя № {} и № {} выполнен.", userId, friendId);
        return commonFriends;
    }

    private void setNameIfNotPresent(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void checkEmail(User user) {
        boolean checkOnExist = userDbStorage.getAll().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()));

        if (checkOnExist) {
            log.warn("Ошибка при выполнении запроса. Email {} занят.", user.getEmail());
            throw new ValidationException("Email " + user.getEmail() + " занят.");
        }
    }
}