package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetPublicUserRequest;
import com.nashss.se.chessplayerservice.activity.response.GetUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetPublicUserActivityTest {

    @Mock
    private UserDao userDao;

    private GetPublicUserActivity getPublicUserActivity;

    @BeforeEach
    void setup() {
        openMocks(this);
        getPublicUserActivity = new GetPublicUserActivity(userDao);
    }

    @Test
    void handleRequest_happy() {
        String username = "username";
        GetPublicUserRequest request = GetPublicUserRequest.builder()
                .withUsername(username)
                .build();
        User user = new User();
        user.setActive("true");
        user.setRating(900);
        user.setUsername(username);
        when(userDao.loadPublicUser(username)).thenReturn(user);

        GetUserResponse response = getPublicUserActivity.handleRequest(request);

        verify(userDao).loadPublicUser(username);
        assertEquals(user, response.getUser());
    }

    @Test
    void handleRequest_usernameIsNull_throwsException() {
        GetPublicUserRequest request = GetPublicUserRequest.builder()
                .build();

        assertThrows(InvalidRequestException.class, () -> getPublicUserActivity.handleRequest(request));
    }

    @Test
    void handleRequest_usernameNotFound_throwsException() {
        GetPublicUserRequest request = GetPublicUserRequest.builder()
                .withUsername("username")
                .build();
        when(userDao.load("username")).thenReturn(null);

        assertThrows(InvalidRequestException.class, () -> getPublicUserActivity.handleRequest(request));
    }
}