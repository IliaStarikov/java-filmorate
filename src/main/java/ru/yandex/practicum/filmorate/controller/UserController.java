package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

/**
 * Контроллер для {@link User}
 */

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long userId = 0L;

    @GetMapping
    public List<User> findAll() {
        log.info("Количество пользователей: {}.", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        user.setId(++userId);
        setNameIfNotPresent(user);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь: {}.", user.getEmail());
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        User existingUser = users.get(user.getId());

        if (existingUser == null) {
            throw new NotFoundException("Пользователь с ID " + user.getId() + " не найден.");
        }
        setNameIfNotPresent(user);
        users.put(user.getId(), user);
        log.info("Обновление пользователя: {}.", existingUser);
        return user;
    }

    private void setNameIfNotPresent(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
