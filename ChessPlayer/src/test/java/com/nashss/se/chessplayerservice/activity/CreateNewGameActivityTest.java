package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.CreateNewGameRequest;
import com.nashss.se.chessplayerservice.activity.response.CreateNewGameResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import com.nashss.se.chessplayerservice.utils.ChessUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CreateNewGameActivityTest {

    @Mock
    private UserDao userDao;

    @Mock
    private GameDao gameDao;

    private CreateNewGameActivity createNewGameActivity;

    @BeforeEach
    void setup() {
        openMocks(this);
        createNewGameActivity = new CreateNewGameActivity(userDao, gameDao);
    }

    @Test
    void handleRequest_happy() {
        CreateNewGameRequest request = CreateNewGameRequest.builder()
                .withBotDifficulty(0)
                .withAuthUserWhite(true)
                .withAuthPlayerUsername("username")
                .build();
        Game game = new Game();
        game.setActive("true");
        game.setBotDifficulty(0);
        game.setNotation(ChessUtils.STARTING_NOTATION);
        game.setValidMoves(ChessUtils.STARTING_VALID_MOVES);
        game.setWhitePlayerUsername("username");
        User user = new User();
        user.setUsername("username");
        when(userDao.load("username")).thenReturn(user);

        CreateNewGameResponse response = createNewGameActivity.handleRequest(request);

        assertNotNull(response.getGameId());
        game.setGameId(response.getGameId());
        verify(gameDao).save(eq(game));
    }

    @Test
    void handleRequest_usernamesAreNull_throwsInvalidRequestException() {
        CreateNewGameRequest request = CreateNewGameRequest.builder()
                .withBotDifficulty(0)
                .withAuthUserWhite(true)
                .build();

        assertThrows(InvalidRequestException.class, () -> createNewGameActivity.handleRequest(request));
    }
}