package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    String inValidUser;
    @Autowired
    private MockMvc mockMvc;
    private static final String CONTENT_TYPE = "application/json";

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
    void updateUser_TrueNotFoundException() throws Exception {
        String updateUser = "{\"id\":\"9999\",\"login\":\"dolor\",\"email\":\"mail@mail.ru\"," +
                "\"birthday\":\"1946-08-20\"}";
        this.mockMvc.perform(put("/users")
                        .contentType(CONTENT_TYPE)
                        .content(updateUser))
                .andExpect(status().isBadRequest());
    }
}