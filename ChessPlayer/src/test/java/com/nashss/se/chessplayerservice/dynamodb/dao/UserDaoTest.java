package com.nashss.se.chessplayerservice.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;

    private UserDao userDao;

    @BeforeEach
    public void setup() {
        openMocks(this);
        userDao = new UserDao(dynamoDBMapper);
    }

    @Test
    void load_happy() {
        String username = "realUsername";
        User user = new User();
        user.setUsername(username);
        user.setRating(900);
        user.setActive("true");
        user.setEmail("realUsername@gmail.com");
        when(dynamoDBMapper.load(User.class, username)).thenReturn(user);

        User result = userDao.load(username);

        assertEquals(user, result);
    }

    @Test
    void load_usernameDoesNotExist_returnsNull() {
        String username = "fakeUsername";
        when(dynamoDBMapper.load(User.class, username)).thenReturn(null);

        User result = userDao.load(username);

        assertNull(result);
    }

    @Test
    void loadPublicUser_happy() {
        String username = "realUsername";
        User userWithEmail = new User();
        userWithEmail.setUsername(username);
        userWithEmail.setRating(900);
        userWithEmail.setActive("true");
        userWithEmail.setEmail("realUsername@gmail.com");
        User userWithoutEmail = new User();
        userWithoutEmail.setUsername(username);
        userWithoutEmail.setRating(900);
        userWithoutEmail.setActive("true");
        when(dynamoDBMapper.load(User.class, username)).thenReturn(userWithEmail);

        User result = userDao.loadPublicUser(username);

        assertEquals(userWithoutEmail, result);
    }

    @Test
    void loadPublicUser_usernameDoesNotExist_returnsNull() {
        String username = "fakeUsername";
        when(dynamoDBMapper.load(User.class, username)).thenReturn(null);

        User result = userDao.load(username);

        assertNull(result);
    }
}