package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {
    String inValidFilm;
    String inValidUser;
    @Autowired
    private MockMvc mockMvc;
    private static final String CONTENT_TYPE = "application/json";

    // Тесты для FilmController
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

        try {
            this.mockMvc.perform(post("/films")
                            .contentType(CONTENT_TYPE)
                            .content(inValidFilm))
                    .andExpect(status().isInternalServerError());
        } catch (NestedServletException ex) {
            assertTrue(ex.getCause() instanceof ValidationException);
        }
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
    void updateFilm_TrueExceptionValidation() throws Exception {
        String updateFilm = "{\"id\":\"9999\",\"name\":\"Name\",\"description\":\"Description\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":120}";
        try {
            this.mockMvc.perform(put("/films")
                            .contentType(CONTENT_TYPE)
                            .content(updateFilm))
                    .andExpect(status().isInternalServerError());
        } catch (NestedServletException ex) {
            assertTrue(ex.getCause() instanceof ValidationException);
        }
    }

    @Test
    void createFilm_getAllFilms_ReturnOkRequestAndEqualsFilm() throws Exception {
        String validFilm = "{\"name\":\"Film Name\",\"description\":\"Description\",\"releaseDate\":\"1895-12-28\"," +
                "\"duration\":100}";

        this.mockMvc.perform(post("/films")
                        .contentType(CONTENT_TYPE)
                        .content(validFilm))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Film Name"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.releaseDate").value("1895-12-28"))
                .andExpect(jsonPath("$.duration").value(100));
    }


    // Тесты для UserController
    @Test
    void createUser_WhiteSpaseLogin_ReturnBadRequest() throws Exception {
        inValidUser = "{\"login\":\"dol ore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";

        mockMvc.perform(post("/users")
                        .contentType(CONTENT_TYPE)
                        .content(inValidUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_LoginNull_ReturnBadRequest() throws Exception {
        inValidUser = "{\"login\":\"\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}";

        mockMvc.perform(post("/users")
                        .contentType(CONTENT_TYPE)
                        .content(inValidUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_EmailNull_ReturnBadRequest() throws Exception {
        inValidUser = "{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"\",\"birthday\":\"1946-08-20\"}";

        mockMvc.perform(post("/users")
                        .contentType(CONTENT_TYPE)
                        .content(inValidUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_EmailNotCorrect_ReturnBadRequest() throws Exception {
        inValidUser = "{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail-mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";

        mockMvc.perform(post("/users")
                        .contentType(CONTENT_TYPE)
                        .content(inValidUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_BirthdayInFuture_ReturnBadRequest() throws Exception {
        inValidUser = "{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"2446-08-20\"}";

        mockMvc.perform(post("/users")
                        .contentType(CONTENT_TYPE)
                        .content(inValidUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_CreateValidUser_ReturnOkRequest() throws Exception {
        String validUser = "{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"2024-02-10\"}";

        mockMvc.perform(post("/users")
                        .contentType(CONTENT_TYPE)
                        .content(validUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.login").value("dolore"))
                .andExpect(jsonPath("$.name").value("Nick Name"))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"))
                .andExpect(jsonPath("$.birthday").value("2024-02-10"));
    }

    @Test
    void createUser_NameNull_LoginEqualsNameTrue() throws Exception {
        String validUser = "{\"login\":\"dolore\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}";

        this.mockMvc.perform(post("/users")
                        .contentType(CONTENT_TYPE)
                        .content(validUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.login").value("dolore"))
                .andExpect(jsonPath("$.name").value("dolore"))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"))
                .andExpect(jsonPath("$.birthday").value("1946-08-20"));
    }

    @Test
    void getAllUser_ReturnUsersAndOkRequestAndEqualsUser() throws Exception {
        String validUser = "{\"login\":\"dolor\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}";
        mockMvc.perform(post("/users")
                        .contentType(CONTENT_TYPE)
                        .content(validUser))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[3].id").value(4))
                .andExpect(jsonPath("$[3].login").value("dolor"))
                .andExpect(jsonPath("$[3].name").value("dolor"))
                .andExpect(jsonPath("$[3].email").value("mail@mail.ru"))
                .andExpect(jsonPath("$[3].birthday").value("1946-08-20"));
    }

    @Test
    void updateUser_PutUser_ReturnOkRequestAndEqualsUser() throws Exception {
        String validUser = "{\"login\":\"dolor\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}";
        mockMvc.perform(post("/users")
                        .contentType(CONTENT_TYPE)
                        .content(validUser))
                .andExpect(status().isOk());

        String updateUser = "{\"id\":\"1\",\"login\":\"dolores\",\"name\":\"Nick Name\"," +
                "\"email\":\"mail1946@mail.ru\",\"birthday\":\"1946-08-20\"}";
        this.mockMvc.perform(put("/users")
                        .contentType(CONTENT_TYPE)
                        .content(updateUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("dolores"))
                .andExpect(jsonPath("$.name").value("Nick Name"))
                .andExpect(jsonPath("$.email").value("mail1946@mail.ru"))
                .andExpect(jsonPath("$.birthday").value("1946-08-20"));
    }

    @Test
    void updateUser_TrueExceptionValidation() throws Exception {
        String updateUser = "{\"id\":\"9999\",\"login\":\"dolor\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";
        try {
            this.mockMvc.perform(put("/users")
                            .contentType(CONTENT_TYPE)
                            .content(updateUser))
                    .andExpect(status().isInternalServerError());
        } catch (NestedServletException ex) {
            assertTrue(ex.getCause() instanceof ValidationException);
        }
    }
}
