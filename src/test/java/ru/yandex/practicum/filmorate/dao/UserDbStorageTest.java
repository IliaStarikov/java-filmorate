package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.users.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private User user;

    @BeforeEach
    public void init() {
        user = User.builder()
                .id(1)
                .name("dot")
                .email("dot1980@mail.ru")
                .login("dot80")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();
    }

    @Test
    public void check_create_shouldAddUser() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        User newUser = userDbStorage.create(user);
        assertEquals(user, newUser);
    }

    @Test
    public void check_update_shouldUpdate() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        User newUser = user.toBuilder().birthday(LocalDate.of(1990, 2, 2)).build();
        userDbStorage.create(user);
        User updatedUser = userDbStorage.update(newUser);
        assertEquals(newUser, updatedUser);
    }

    @Test
    public void check_getUser_shouldFind() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        userDbStorage.create(user);
        User findUser = userDbStorage.getUser(1).get();
        assertEquals(user, findUser);
    }

    @Test
    public void check_deleteUser_shouldDelete() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        userDbStorage.create(user);
        User deletedUser = userDbStorage.deleteUser(1);
        assertEquals(user, deletedUser);
    }

    @Test
    public void check_getAllUsers_shouldReturnListSize_3() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        User user2 = user.toBuilder().name("Mark").email("mark111@mail.ru").build();
        User user3 = user.toBuilder().name("Max").email("max222@mail.ru").build();

        userDbStorage.create(user);
        userDbStorage.create(user2);
        userDbStorage.create(user3);

        int size = userDbStorage.getAll().size();

        assertEquals(3, size);
    }

    @Test
    public void check_user1_Friendship_to_user2() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        FriendshipDbStorage friendshipDbStorage = new FriendshipDbStorage(jdbcTemplate);
        User user2 = user.toBuilder().name("Alex").email("refds@mail.ru").build();

        User user1InDb = userDbStorage.create(user);
        User user2InDb = userDbStorage.create(user2);

        friendshipDbStorage.addFriend(1, 2);

        List<User> listFriendUser1 = friendshipDbStorage.getUserFriends(1);

        int countFriendUser1 = listFriendUser1.size();

        assertEquals(1, countFriendUser1);

        User friend = listFriendUser1.get(0);

        assertEquals(user2InDb, friend);

        int countFriendUser2 = friendshipDbStorage.getUserFriends(2).size();

        assertEquals(0, countFriendUser2);

        friendshipDbStorage.addFriend(2, 1);

        List<User> listFriendUser2 = friendshipDbStorage.getUserFriends(2);

        int size = listFriendUser2.size();

        assertEquals(1, size);

        assertEquals(user1InDb, listFriendUser2.get(0));
    }
}
