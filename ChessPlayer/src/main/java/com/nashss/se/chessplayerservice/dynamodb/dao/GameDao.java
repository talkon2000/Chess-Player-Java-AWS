package com.nashss.se.chessplayerservice.dynamodb.dao;

import com.nashss.se.chessplayerservice.dynamodb.models.Game;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameDao {

    @Inject
    public GameDao() {}

    public Game get(String gameId) {
        Game game = new Game();
        game.setGameId(gameId);
        game.setMoves("startpos moves d2d4");
        game.setWhitePlayerId("1");
        game.setBlackPlayerId("2");
        return game;
    }
}
