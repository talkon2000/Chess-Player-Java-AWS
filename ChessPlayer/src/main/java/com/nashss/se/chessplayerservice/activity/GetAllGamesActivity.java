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

/**
 * Implementation of the GetAllGamesActivity for the ChessPlayerClient's GetAllGames API.
 *
 * This API allows the user to get all games associated with their username.
 */
public class GetAllGamesActivity {

    private final UserDao userDao;
    private final GameDao gameDao;

    /**
     * Instantiates a new GetAllGamesActivity object.
     *
     * @param gameDao DAO to access the games table.
     * @param userDao DAO to access the users table.
     */
    @Inject
    public GetAllGamesActivity(UserDao userDao, GameDao gameDao) {
        this.userDao = userDao;
        this.gameDao = gameDao;
    }

    /**
     * This method handles the incoming request by getting the games from the database.
     * <p>
     * It then returns the {@link Game} objects.
     * <p>
     * If the username is not valid, this should throw an InvalidRequestException.
     *
     * @param request request object containing the username
     * @return GetAllGamesResponse object containing the list of {@link Game}s
     */
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
