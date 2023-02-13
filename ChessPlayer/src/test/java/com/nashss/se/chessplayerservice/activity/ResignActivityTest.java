package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.ResignRequest;
import com.nashss.se.chessplayerservice.activity.response.ResignResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class ResignActivityTest {

    @Mock
    private GameDao gameDao;

    private ResignActivity resignActivity;

    @BeforeEach
    void setup() {
        openMocks(this);
        resignActivity = new ResignActivity(gameDao);
    }

    @Test
    void handleRequest_happy() {
        ResignRequest request = ResignRequest.builder()
                .withGameId("gameId")
                .withUsername("username")
                .build();
        Game game = new Game();
        game.setGameId("gameId");
        game.setActive("true");
        game.setWhitePlayerUsername("notUsername");
        game.setBlackPlayerUsername("username");
        Game resignedGame = new Game();
        resignedGame.setGameId("gameId");
        resignedGame.setActive("false");
        resignedGame.setWhitePlayerUsername("notUsername");
        resignedGame.setBlackPlayerUsername("username");
        resignedGame.setWinner("white");
        when(gameDao.load("gameId")).thenReturn(game);

        ResignResponse response = resignActivity.handleRequest(request);

        verify(gameDao).load("gameId");
        verify(gameDao).save(eq(resignedGame));
        assertEquals(resignedGame, response.getGame());
    }

    @Test
    void handleRequest_gameIdIsNull_throwsException() {
        ResignRequest request = ResignRequest.builder()
                .withGameId(null)
                .withUsername("username")
                .build();

        assertThrows(InvalidRequestException.class, () -> resignActivity.handleRequest(request));
    }

    @Test
    void handleRequest_usernameIsNull_throwsException() {
        ResignRequest request = ResignRequest.builder()
                .withGameId("gameId")
                .withUsername(null)
                .build();

        assertThrows(InvalidRequestException.class, () -> resignActivity.handleRequest(request));
    }

    @Test
    void handleRequest_gameDoesNotExist_throwsException() {
        ResignRequest request = ResignRequest.builder()
                .withGameId("gameId")
                .withUsername("username")
                .build();
        when(gameDao.load("gameId")).thenReturn(null);

        assertThrows(InvalidRequestException.class, () -> resignActivity.handleRequest(request));
        verify(gameDao).load("gameId");
    }

    @Test
    void handleRequest_usernameDoesNotBelongToGame_throwsException() {
        ResignRequest request = ResignRequest.builder()
                .withGameId("gameId")
                .withUsername("username")
                .build();
        Game game = new Game();
        game.setActive("true");
        game.setWhitePlayerUsername("notUsername");
        game.setBlackPlayerUsername("alsoNotUsername");
        when(gameDao.load("gameId")).thenReturn(game);

        assertThrows(InvalidRequestException.class, () -> resignActivity.handleRequest(request));
        verify(gameDao).load("gameId");
    }
}