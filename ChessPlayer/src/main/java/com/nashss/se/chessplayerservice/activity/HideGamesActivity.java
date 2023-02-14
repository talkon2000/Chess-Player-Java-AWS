package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.HideGamesRequest;
import com.nashss.se.chessplayerservice.activity.response.HideGamesResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;

import javax.inject.Inject;

public class HideGamesActivity {

    private final GameDao gameDao;

    @Inject
    public HideGamesActivity(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public HideGamesResponse handleRequest(HideGamesRequest request) {
        return null;
    }
}
