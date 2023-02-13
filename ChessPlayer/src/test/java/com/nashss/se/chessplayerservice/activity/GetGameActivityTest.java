package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetGameRequest;
import com.nashss.se.chessplayerservice.activity.response.GetGameResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetGameActivityTest {

    @Mock
    private GameDao gameDao;

    private GetGameActivity getGameActivity;

    @BeforeEach
    void setup() {
        openMocks(this);
        getGameActivity = new GetGameActivity(gameDao);
    }

    @Test
    void handleRequest_happy() {
        String gameId = "gameId";
        Game game = new Game();
        game.setGameId(gameId);
        game.setActive("true");
        GetGameRequest request = GetGameRequest.builder()
                .withGameId(gameId)
                .build();
        when(gameDao.load(gameId)).thenReturn(game);

        GetGameResponse response = getGameActivity.handleRequest(request);

        assertEquals(game, response.getGame());
    }

    @Test
    void handleRequest_gameIdIsNull_throwsException() {
        GetGameRequest request = GetGameRequest.builder()
                .build();

        assertThrows(InvalidRequestException.class, () -> getGameActivity.handleRequest(request));
    }

    @Test
    void handleRequest_gameDoesNotExist_throwsException() {
        GetGameRequest request = GetGameRequest.builder()
                .withGameId("gameId")
                .build();
        when(gameDao.load("gameId")).thenReturn(null);

        assertThrows(InvalidRequestException.class, () -> getGameActivity.handleRequest(request));

        verify(gameDao).load("gameId");
    }
}