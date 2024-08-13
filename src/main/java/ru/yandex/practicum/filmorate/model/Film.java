package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder(toBuilder = true)
public class Film {
    private final long id;

    @NotBlank(message = "Название фильма не может быть пустым!")
    private String name;

    @Size(min = 1, max = 200, message = "Максимальная длина описания - 200 символов!")
    private String description;

    @NotNull(message = "Релиз фильма не может быть - null!")
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность фильма не может быть - null!")
    @Positive(message = "Продолжительность фильма должна быть больше нуля!")
    private int duration;

    @NotNull
    private MPA mpa;

    private final Set<Genre> genres = new LinkedHashSet<>();

    @Getter
    private Set<Long> likes;

    public void addGenre(Set<Genre> genres) {
        this.genres.addAll(genres);
    }
}
