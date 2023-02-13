package com.nashss.se.chessplayerservice.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GameDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;

    private GameDao gameDao;

    @BeforeEach
    public void setup() {
        openMocks(this);
        gameDao = new GameDao(dynamoDBMapper);
    }

    @Test
    void load_happy() {
        String gameId = "realGameId";
        Game game = new Game();
        game.setGameId(gameId);
        game.setActive("true");
        game.setNotation("startpos");
        game.setWhitePlayerUsername("username");
        game.setBotDifficulty(9);
        game.setMoves("d2d4");
        when(dynamoDBMapper.load(Game.class, gameId)).thenReturn(game);

        Game result = gameDao.load(gameId);

        assertEquals(game, result);
    }

    @Test
    void load_gameIdDoesNotExist_returnsNull() {
        String gameId = "notAGameId";
        when(dynamoDBMapper.load(Game.class, gameId)).thenReturn(null);

        Game result = gameDao.load(gameId);

        assertNull(result);
    }
}