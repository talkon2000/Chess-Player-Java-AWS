package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetAllGamesRequest;
import com.nashss.se.chessplayerservice.activity.response.GetAllGamesResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GetAllGamesActivity {

    private final UserDao userDao;
    private final GameDao gameDao;

    @Inject
    public GetAllGamesActivity(UserDao userDao, GameDao gameDao) {
        this.userDao = userDao;
        this.gameDao = gameDao;
    }

    public GetAllGamesResponse handleRequest(GetAllGamesRequest request) {
        if (request.getUsername() == null) {
            throw new InvalidRequestException("Username cannot be null");
        }

        User user = userDao.loadPublicUser(request.getUsername());
        Set<Game> games = new HashSet<>();
        if (user.getGames() != null) {
            games = user.getGames().parallelStream().map(gameDao::load).collect(Collectors.toSet());
        }

        return GetAllGamesResponse.builder()
                .withGames(games)
                .build();
    }
}
