package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    String inValidFilm;
    @Autowired
    private MockMvc mockMvc;
    private static final String CONTENT_TYPE = "application/json";

    @Test
    void createFilm_WithInvalidName_ReturnBadRequest() throws Exception {
        inValidFilm = "{\"name\":\"\",\"description\":\"Description\",\"releaseDate\":\"1895-12-29\",\"duration\":1}";

        mockMvc.perform(post("/films")
                        .contentType(CONTENT_TYPE)
                        .content(inValidFilm))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_WithInvalidDescription_ReturnBadRequest() throws Exception {
        inValidFilm = "{\"name\":\"Film Name\",\"description\":\"Lorem ipsum dolor sit amet, consectetur adipiscing" +
                " elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\"," +
                "\"releaseDate\":\"1895-12-29\",\"duration\":100}";
        mockMvc.perform(post("/films")
                        .contentType(CONTENT_TYPE)
                        .content(inValidFilm))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_WithInvalidReleaseDateReturnBadRequest() throws Exception {
        inValidFilm = "{\"name\":\"Film Name\",\"description\":\"Film description\",\"releaseDate\":\"1895-12-27\"," +
                "\"duration\":100}";

        this.mockMvc.perform(post("/films")
                        .contentType(CONTENT_TYPE)
                        .content(inValidFilm))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_WithInvalidReleaseDateReturnExceptionValidationTrue() throws Exception {
        inValidFilm = "{\"name\":\"Film Name\",\"description\":\"Film description\",\"releaseDate\":null," +
                "\"duration\":100}";

        mockMvc.perform(post("/films")
                        .contentType(CONTENT_TYPE)
                        .content(inValidFilm))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_WithInvalidDurationReturnBadRequest() throws Exception {
        inValidFilm = "{\"name\":\"Film Name\",\"description\":\"Film description\",\"releaseDate\":\"1895-12-29\"," +
                "\"duration\":0}";

        mockMvc.perform(post("/films")
                        .contentType(CONTENT_TYPE)
                        .content(inValidFilm))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllFilm_ReturnFilmsAndOkRequestAndEqualsFilm() throws Exception {
        String validFilm = "{\"name\":\"Film Name\",\"description\":\"Description\",\"releaseDate\":\"2015-03-01\"," +
                "\"duration\":100}";
        mockMvc.perform(post("/films")
                        .contentType(CONTENT_TYPE)
                        .content(validFilm))
                .andExpect(status().isOk());

        final MvcResult mvcResult = this.mockMvc.perform(get("/films"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Film Name"))
                .andExpect(jsonPath("$[1].description").value("Description"))
                .andExpect(jsonPath("$[1].releaseDate").value("2015-03-01"))
                .andExpect(jsonPath("$[1].duration").value(100))
                .andReturn();
        assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType());
    }

    @Test
    void updateFilm_ReturnOkRequestAndEqualsFilm() throws Exception {
        String film = "{\"name\":\"Film Name\",\"description\":\"Description\",\"releaseDate\":\"1895-12-28\"," +
                "\"duration\":100}";
        mockMvc.perform(post("/films")
                        .contentType(CONTENT_TYPE)
                        .content(film))
                .andExpect(status().isOk());

        String updateFilm = "{\"id\":\"1\",\"name\":\"Name\",\"description\":\"Description\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":120}";
        this.mockMvc.perform(put("/films")
                        .contentType(CONTENT_TYPE)
                        .content(updateFilm))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.releaseDate").value("1895-12-28"))
                .andExpect(jsonPath("$.duration").value(120));
    }

    @Test
    void updateFilm_TrueNotFoundException() throws Exception {
        String updateFilm = "{\"id\":\"9999\",\"name\":\"Name\",\"description\":\"Description\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":120}";
        try {
            this.mockMvc.perform(put("/films")
                            .contentType(CONTENT_TYPE)
                            .content(updateFilm))
                    .andExpect(status().isNotFound());
        } catch (NestedServletException ex) {
            assertTrue(ex.getCause() instanceof NotFoundException);
        }
    }

    @Test
    void updateFilm_inValidReleaseDateReturnBadRequest() throws Exception {
        String validFilm = "{\"name\":\"Name\",\"description\":\"Description\"," +
                "\"releaseDate\":\"1895-12-29\",\"duration\":120}";
        mockMvc.perform(post("/films")
                        .contentType(CONTENT_TYPE)
                        .content(validFilm))
                .andExpect(status().isOk());

        inValidFilm = "{\"id\":\"1\",\"name\":\"Name\",\"description\":\"Description\"," +
                "\"releaseDate\":\"1895-12-27\",\"duration\":120}";
        this.mockMvc.perform(put("/films")
                        .contentType(CONTENT_TYPE)
                        .content(inValidFilm))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_getAllFilms_ReturnOkRequestAndEqualsFilm() throws Exception {
        String validFilm = "{\"name\":\"Film Name\",\"description\":\"Description\",\"releaseDate\":\"1895-12-28\"," +
                "\"duration\":100}";

        this.mockMvc.perform(post("/films")
                        .contentType(CONTENT_TYPE)
                        .content(validFilm))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("Film Name"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.releaseDate").value("1895-12-28"))
                .andExpect(jsonPath("$.duration").value(100));
    }
}