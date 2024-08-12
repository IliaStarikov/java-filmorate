package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Builder;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * User.
 */
@Data
@Builder(toBuilder = true)
public class User {

    private final int id;
    private String name;

    @Email(message = "Неверный формат электронной почты")
    @NotBlank(message = "Электронная почта не может быть пустой")
    private String email;

    @NotBlank(message = "Login не может быть пустым!")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    private String login;

    @Past(message = "Дата рождения не может быть в будущем")
    @NotNull
    private LocalDate birthday;
}