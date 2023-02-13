package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.ResignRequest;
import com.nashss.se.chessplayerservice.activity.response.ResignResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;

import javax.inject.Inject;

/**
 * Implementation of the ResignActivity for the ChessPlayerClient's Resign API.
 *
 * This API allows the user resign their current game.
 */
public class ResignActivity {

    private final GameDao gameDao;

    /**
     * Instantiates a new ResignActivity object.
     *
     * @param gameDao DAO to access the games table.
     */
    @Inject
    public ResignActivity(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    /**
     * This method handles the incoming request by updating the {@link Game} in the database to reflect a resignation.
     * <p>
     * It then returns the game object.
     * <p>
     * If the username is null or does not belong to the game, this should throw an InvalidRequestException.
     * <p>
     * If the gameId is null or does not exist in the database, this should throw an InvalidRequestException.
     *
     * @param request request object containing the gameId and username
     * @return ResignResponse object containing the {@link Game}
     */
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
        } else if (game.getBlackPlayerUsername() != null &&
                 game.getBlackPlayerUsername().equals(request.getUsername())) {
            game.setActive("false");
            game.setWinner("white");
        } else {
            throw new InvalidRequestException("Username must belong to a user playing the game");
        }

        gameDao.save(game);

        return ResignResponse.builder()
                .withGame(game)
                .build();
    }
}
