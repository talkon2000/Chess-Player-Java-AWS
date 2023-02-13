package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.ResignRequest;
import com.nashss.se.chessplayerservice.activity.response.ResignResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;

import javax.inject.Inject;

public class ResignActivity {

    private final GameDao gameDao;

    @Inject
    public ResignActivity(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public ResignResponse handleRequest(ResignRequest request) {
        if (request.getGameId() == null) {
            throw new InvalidRequestException("Game ID can not be null");
        }

        if (request.getUsername() == null) {
            throw new InvalidRequestException("Username can not be null");
        }

        Game game = gameDao.load(request.getGameId());
        if (game == null) {
            throw new InvalidRequestException("There is no game with that ID");
        }

        if (game.getWhitePlayerUsername() != null && game.getWhitePlayerUsername().equals(request.getUsername())) {
            game.setActive("false");
            game.setWinner("black");
        }
        else if (game.getBlackPlayerUsername() != null && game.getBlackPlayerUsername().equals(request.getUsername())) {
            game.setActive("false");
            game.setWinner("white");
        }
        else {
            throw new InvalidRequestException("Username must belong to a user playing the game");
        }

        gameDao.save(game);

        return ResignResponse.builder()
                .withGame(game)
                .build();
    }
}
