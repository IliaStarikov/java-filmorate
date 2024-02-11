package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@AllArgsConstructor
public class Film {
    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым!")
    private String name;
    @Size(min = 1, max = 200, message = "Максимальная длина описания - 200 символов!")
    private String description;
    @NotNull(message = "Релиз фильма не может быть - null!")
    private LocalDate releaseDate;
    @NotNull(message = "Продолжительность фильма не может быть - null!")
    @Positive(message = "Продолжительность фильма должна быть больше нуля!")
    private int duration;

}
