package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetGameRequest;
import com.nashss.se.chessplayerservice.activity.response.GetGameResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;

import javax.inject.Inject;

/**
 * Implementation of the GetGameActivity for the ChessPlayerClient's GetGame API.
 *
 * This API allows the user to get a {@link Game} from a gameId.
 */
public class GetGameActivity {
    private final GameDao gameDao;

    /**
     * Instantiates a new GetGameActivity object.
     *
     * @param gameDao DAO to access the games table.
     */
    @Inject
    public GetGameActivity(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    /**
     * This method handles the incoming request by retrieving the {@link Game} from the database.
     * <p>
     * It then returns the game object.
     * <p>
     * If the gameId is null or the game does not exist, this should throw an InvalidRequestException.
     *
     * @param request request object containing the gameId
     * @return GetGameResponse object containing the {@link Game}
     */
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
