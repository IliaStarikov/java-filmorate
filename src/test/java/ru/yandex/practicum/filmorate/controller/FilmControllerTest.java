package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void createFilm_WithInvalidName_ReturnBadRequest() throws Exception {
        Film film = Film.builder()
                .name("")
                .description("Description")
                .duration(1)
                .releaseDate(LocalDate.of(1895, 12, 29))
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_WithInvalidDescription_ReturnBadRequest() throws Exception {
        Film film = Film.builder()
                .name("Film Name")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing\" +\n" +
                        "\" elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, \" +\n" +
                        "\"quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.")
                .duration(100)
                .releaseDate(LocalDate.of(1895, 12, 29))
                .build();
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_WithInvalidReleaseDateReturnBadRequest() throws Exception {
        Film film = Film.builder()
                .name("Film Name")
                .description("Film description")
                .duration(100)
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build();

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_WithInvalidReleaseDateReturnExceptionValidationTrue() throws Exception {
        Film film = Film.builder()
                .name("Film Name")
                .description("Film description")
                .duration(100)
                .build();

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_WithInvalidDurationReturnBadRequest() throws Exception {
        Film film = Film.builder()
                .name("Film Name")
                .description("Film description")
                .duration(0)
                .releaseDate(LocalDate.of(1895, 12, 29))
                .build();

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }
}