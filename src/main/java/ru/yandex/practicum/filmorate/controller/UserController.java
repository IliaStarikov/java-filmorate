package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

/**
 * Контроллер для {@link User}
 */

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {

    UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        return userService.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return userService.put(user);
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable("id") Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void putFriend(
            @PathVariable("id") Long userId,
            @PathVariable("friendId") Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable("id") Long userId,
            @PathVariable("friendId") Long friendId) {
        userService.endFriendship(userId, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") Long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable("id") Long userId,
            @PathVariable("otherId") Long secondUserId) {
        return userService.getCommonFriends(userId, secondUserId);
    }
}
