package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        return userService.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable("id") Integer userId) {
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{id}")
    public User deleteUserByID(@PathVariable("id") Integer userId) {
        return userService.deleteUser(userId);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void putFriend(
            @PathVariable("id") Integer userId,
            @PathVariable("friendId") Integer friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable("id") Integer userId,
            @PathVariable("friendId") Integer friendId) {
        userService.endFriendship(userId, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") Integer userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable("id") Integer userId,
            @PathVariable("otherId") Integer secondUserId) {
        return userService.getCommonFriends(userId, secondUserId);
    }
}
