package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetGameRequest;
import com.nashss.se.chessplayerservice.activity.response.GetGameResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;

import javax.inject.Inject;

public class GetGameActivity {
    private final GameDao gameDao;

    @Inject
    public GetGameActivity(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public GetGameResponse handleRequest(GetGameRequest request) {
        if (request.getGameId() == null) {
            throw new InvalidRequestException("Game ID must be present");
        }
        Game game = gameDao.load(request.getGameId());

        if (game == null) {
            throw new InvalidRequestException("A game with that ID does not exist.");
        }

        return GetGameResponse.builder().withGame(game).build();
    }
}
