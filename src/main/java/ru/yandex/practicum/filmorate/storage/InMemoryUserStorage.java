package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long userId = 0L;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(++userId);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь: {}.", user.getEmail());
        return user;
    }

    @Override
    public User put(User user) {
        checkUserExists(user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь {} обновлён.", user.getLogin());
        return user;
    }

    @Override
    public User getUser(Long userId) {
        checkUserExists(userId);
        return users.get(userId);
    }

    public void checkUserExists(Long... userIds) throws NotFoundException {
        for (Long userId : userIds) {
            if (!users.containsKey(userId)) {
                log.warn("Пользователь с ID: {} не найден.", userId);
                throw new NotFoundException(String.format("Пользователь с ID: %s не найден.", userId));
            }
        }
    }
}
