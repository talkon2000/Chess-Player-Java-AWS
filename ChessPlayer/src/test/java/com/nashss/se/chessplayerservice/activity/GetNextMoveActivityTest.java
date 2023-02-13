package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetNextMoveRequest;
import com.nashss.se.chessplayerservice.activity.response.GetNextMoveResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.engine.Stockfish;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import com.nashss.se.chessplayerservice.exceptions.StockfishException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetNextMoveActivityTest {

    @Mock
    private UserDao userDao;

    @Mock
    private GameDao gameDao;

    @Mock
    private Stockfish stockfish;

    private GetNextMoveActivity getNextMoveActivity;

    @BeforeEach
    void setup() {
        openMocks(this);
        getNextMoveActivity = new GetNextMoveActivity(gameDao, userDao, stockfish);
    }

    @Test
    void handleRequest_moveIsNull_throwsException() {
        GetNextMoveRequest request = GetNextMoveRequest.builder()
                .withGameId("gameId")
                .build();

        assertThrows(InvalidRequestException.class, () -> getNextMoveActivity.handleRequest(request));
    }

    @Test
    void handleRequest_gameIdIsNull_throwsException() {
        GetNextMoveRequest request = GetNextMoveRequest.builder()
                .withMove("move")
                .build();

        assertThrows(InvalidRequestException.class, () -> getNextMoveActivity.handleRequest(request));
    }

    @Test
    void handleRequest_gameDoesNotExist_throwsException() {
        GetNextMoveRequest request = GetNextMoveRequest.builder()
                .withGameId("gameId")
                .withMove("move")
                .build();
        when(gameDao.load("gameId")).thenReturn(null);

        assertThrows(InvalidRequestException.class, () -> getNextMoveActivity.handleRequest(request));
    }

    @Test
    void handleRequest_gameIsInactive_throwsException() {
        GetNextMoveRequest request = GetNextMoveRequest.builder()
                .withGameId("gameId")
                .withMove("move")
                .build();
        Game game = new Game();
        game.setActive("false");
        when(gameDao.load("gameId")).thenReturn(game);
        when(gameDao.load("gameId")).thenReturn(null);

        assertThrows(InvalidRequestException.class, () -> getNextMoveActivity.handleRequest(request));
    }

    @Test
    void handleRequest_stockfishFailsToStart_throwsException() {
        GetNextMoveRequest request = GetNextMoveRequest.builder()
                .withGameId("gameId")
                .withMove("move")
                .build();
        Game game = new Game();
        game.setActive("true");
        when(gameDao.load("gameId")).thenReturn(game);
        when(stockfish.startEngine()).thenReturn(false);

        assertThrows(StockfishException.class, () -> getNextMoveActivity.handleRequest(request));
    }

    @Test
    void handleRequest_stockfishIllegalMove_throwsException() {
        GetNextMoveRequest request = GetNextMoveRequest.builder()
                .withGameId("gameId")
                .withMove("move")
                .build();
        Game game = new Game();
        game.setActive("true");
        game.setNotation("notation");
        when(gameDao.load("gameId")).thenReturn(game);
        when(stockfish.startEngine()).thenReturn(true);
        when(stockfish.getLegalMoves("fen notation")).thenReturn(List.of("move1", "move2"));

        assertThrows(InvalidRequestException.class, () -> getNextMoveActivity.handleRequest(request));
        verify(stockfish).getLegalMoves("fen notation");
    }
}