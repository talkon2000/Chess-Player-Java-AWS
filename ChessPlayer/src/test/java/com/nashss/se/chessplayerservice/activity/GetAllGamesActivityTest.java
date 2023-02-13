package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetAllGamesRequest;
import com.nashss.se.chessplayerservice.activity.response.GetAllGamesResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetAllGamesActivityTest {

    @Mock
    private UserDao userDao;

    @Mock
    private GameDao gameDao;

    private GetAllGamesActivity getAllGamesActivity;

    @BeforeEach
    void setup() {
        openMocks(this);
        getAllGamesActivity = new GetAllGamesActivity(userDao, gameDao);
    }

    @Test
    void handleRequest_happy() {
        String username = "username";
        Game game = new Game();
        game.setGameId("gameId");
        User user = new User();
        user.setUsername("username");
        user.setGames(Set.of("gameId"));
        GetAllGamesRequest request = GetAllGamesRequest.builder()
                .withUsername(username)
                .build();
        when(userDao.loadPublicUser(username)).thenReturn(user);
        when(gameDao.load("gameId")).thenReturn(game);

        GetAllGamesResponse response = getAllGamesActivity.handleRequest(request);

        verify(userDao).loadPublicUser(username);
        verify(gameDao).load("gameId");
        assertEquals(game, response.getGames().toArray()[0]);
    }

    @Test
    void handleRequest_usernameIsNull_throwsException() {
        GetAllGamesRequest request = GetAllGamesRequest.builder()
                .build();

        assertThrows(InvalidRequestException.class, () -> getAllGamesActivity.handleRequest(request));
    }
}