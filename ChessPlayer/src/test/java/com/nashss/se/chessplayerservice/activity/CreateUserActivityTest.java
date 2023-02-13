package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.CreateUserRequest;
import com.nashss.se.chessplayerservice.activity.response.CreateUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CreateUserActivityTest {

    @Mock
    private UserDao userDao;

    private CreateUserActivity createUserActivity;

    @BeforeEach
    void setup() {
        openMocks(this);
        createUserActivity = new CreateUserActivity(userDao);
    }

    @Test
    void handleRequest_happy() {
        String username = "username";
        String email = "username@email.com";
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setActive("true");
        user.setRating(900);
        CreateUserRequest request = CreateUserRequest.builder()
                .withUsername(username)
                .withEmail(email)
                .build();

        CreateUserResponse response = createUserActivity.handleRequest(request);

        assertEquals(user, response.getUser());
    }

    @Test
    void handleRequest_usernameIsNull_throwsException() {
        String email = "username@email.com";
        CreateUserRequest request = CreateUserRequest.builder()
                .withEmail(email)
                .build();

        assertThrows(InvalidRequestException.class, () -> createUserActivity.handleRequest(request));
    }

    @Test
    void handleRequest_usernameIsTaken_throwsException() {
        String username = "takenUsername";
        String email = "username@email.com";
        CreateUserRequest request = CreateUserRequest.builder()
                .withUsername(username)
                .withEmail(email)
                .build();
        when(userDao.load(username)).thenReturn(new User());

        assertThrows(InvalidRequestException.class, () -> createUserActivity.handleRequest(request));
    }

    @Test
    void handleRequest_emailIsNull_throwsException() {
        String username = "username";
        CreateUserRequest request = CreateUserRequest.builder()
                .withUsername(username)
                .build();

        assertThrows(InvalidRequestException.class, () -> createUserActivity.handleRequest(request));
    }

    @Test
    void handleRequest_emailIsInvalid_throwsException() {
        String username = "username";
        String email = "username+email.com";
        CreateUserRequest request = CreateUserRequest.builder()
                .withUsername(username)
                .withEmail(email)
                .build();

        assertThrows(InvalidRequestException.class, () -> createUserActivity.handleRequest(request));
    }
}