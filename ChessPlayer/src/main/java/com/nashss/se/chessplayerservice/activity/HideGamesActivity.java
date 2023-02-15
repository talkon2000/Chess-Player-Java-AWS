package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.HideGamesRequest;
import com.nashss.se.chessplayerservice.activity.response.HideGamesResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Implementation of the HideGamesActivity for the ChessPlayerClient's HideGames API.
 *
 * This API allows the user to soft-delete a game entry from the games table.
 */
public class HideGamesActivity {

    private final GameDao gameDao;

    /**
     * Instantiates a new HideGamesActivity object.
     *
     * @param gameDao DAO to access the games table.
     */
    @Inject
    public HideGamesActivity(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    /**
     * This method handles the incoming request by setting the "isVisible" attribute of the {@link Game}
     * object from the database to false.
     * <p>
     * It then returns the gameIds of all the games that were successfully set.
     * <p>
     * If the array of gameIds is null, this should throw an InvalidRequestException.
     *
     * @param request request object containing the array of gameIds
     * @return HideGamesResponse containing the array of gameIds
     */
    public HideGamesResponse handleRequest(HideGamesRequest request) {
        if (request.getGameIds() == null) {
            throw new InvalidRequestException("gameIds cannot be null");
        }

        List<Game> games = Arrays.stream(request.getGameIds()).parallel()
                .map(gameDao::load)
                .filter(Objects::nonNull)
                .peek(game -> game.setIsVisible("false"))
                .peek(gameDao::save)
                .collect(Collectors.toList());

        String[] gameIds = new String[games.size()];
        games.stream().map(Game::getGameId).collect(Collectors.toList()).toArray(gameIds);

        return HideGamesResponse.builder()
                .withGameIds(gameIds)
                .build();
    }
}
