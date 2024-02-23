package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public List<User> findAll();
    public User create(final User user);
    public User put(User user);
    User getUser(Long id);
    void checkUserExists(Long... userIds);
}
