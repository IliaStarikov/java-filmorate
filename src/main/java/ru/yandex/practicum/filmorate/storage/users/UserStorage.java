package ru.yandex.practicum.filmorate.storage.users;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getAll();

    User create(final User user);

    User update(User user);

    Optional<User> getUser(Integer id);

    User deleteUser(Integer id);
}
