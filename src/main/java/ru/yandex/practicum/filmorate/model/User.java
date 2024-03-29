package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * User.
 */
@Data
public class User {

    private long id;
    private String name;

    @Email(message = "Неверный формат электронной почты")
    @NotBlank(message = "Электронная почта не может быть пустой")
    private String email;

    @NotBlank(message = "Login не может быть пустым!")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    private String login;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    @Getter
    private Set<Long> friends = new HashSet<>();

    public void addFriends(Long idFriend) {
        friends.add(idFriend);
    }

    public void deleteFriend(Long idFriend) {
        friends.remove(idFriend);
    }
}
