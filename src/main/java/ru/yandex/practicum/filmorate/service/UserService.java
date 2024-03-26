package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        setNameIfNotPresent(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        setNameIfNotPresent(user);
        return userStorage.put(user);
    }

    public User getUserById(Long id) {
        return userStorage.getUser(id);
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.checkUserExists(userId, friendId);

        userStorage.getUser(userId).addFriends(friendId);
        userStorage.getUser(friendId).addFriends(userId);
    }

    public void endFriendship(Long userId, Long friendId) {
        userStorage.checkUserExists(userId, friendId);
        userStorage.getUser(userId).deleteFriend(friendId);
    }

    public List<User> getFriends(Long userId) {
        userStorage.checkUserExists(userId);
        Set<Long> friendIds = userStorage.getUser(userId).getFriends();

        return friendIds.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        userStorage.checkUserExists(userId, friendId);

        Set<Long> userFriends = userStorage.getUser(userId).getFriends();
        Set<Long> friendFriends = userStorage.getUser(friendId).getFriends();

        return userFriends.stream()
                .filter(friendFriends::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    private void setNameIfNotPresent(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
