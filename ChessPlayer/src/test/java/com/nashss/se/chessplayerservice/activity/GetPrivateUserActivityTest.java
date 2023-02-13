package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetPrivateUserRequest;
import com.nashss.se.chessplayerservice.activity.response.GetUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetPrivateUserActivityTest {

    @Mock
    private UserDao userDao;

    private GetPrivateUserActivity getPrivateUserActivity;

    @BeforeEach
    void setup() {
        openMocks(this);
        getPrivateUserActivity = new GetPrivateUserActivity(userDao);
    }

    @Test
    void handleRequest_happy() {
        String username = "username";
        GetPrivateUserRequest request = GetPrivateUserRequest.builder()
                .withUsername(username)
                .build();
        User user = new User();
        user.setActive("true");
        user.setRating(900);
        user.setEmail("username@email.com");
        user.setUsername(username);
        when(userDao.load(username)).thenReturn(user);

        GetUserResponse response = getPrivateUserActivity.handleRequest(request);

        verify(userDao).load(username);
        assertEquals(user, response.getUser());
    }

    @Test
    void handleRequest_usernameIsNull_throwsException() {
        GetPrivateUserRequest request = GetPrivateUserRequest.builder()
                .build();

        assertThrows(InvalidRequestException.class, () -> getPrivateUserActivity.handleRequest(request));
    }

    @Test
    void handleRequest_usernameNotFound_throwsException() {
        GetPrivateUserRequest request = GetPrivateUserRequest.builder()
                .withUsername("username")
                .build();
        when(userDao.load("username")).thenReturn(null);

        assertThrows(InvalidRequestException.class, () -> getPrivateUserActivity.handleRequest(request));
    }
}