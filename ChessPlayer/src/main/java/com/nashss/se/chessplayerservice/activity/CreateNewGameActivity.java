package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.CreateNewGameRequest;
import com.nashss.se.chessplayerservice.activity.response.CreateNewGameResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import com.nashss.se.chessplayerservice.utils.ChessUtils;

import java.util.Set;

import javax.inject.Inject;

/**
 * Implementation of the CreateNewGameActivity for the ChessPlayerClient's CreateNewGame API.
 *
 * This API allows the user to create a new game, creates the game in the database, and returns the game.
 * Also updates the user with the gameId.
 */
public class CreateNewGameActivity {

    private final UserDao userDao;
    private final GameDao gameDao;

    /**
     * Instantiates a new CreateNewGameActivity object.
     *
     * @param gameDao DAO to access the games table.
     * @param userDao DAO to access the users table.
     */
    @Inject
    public CreateNewGameActivity(UserDao userDao, GameDao gameDao) {
        this.userDao = userDao;
        this.gameDao = gameDao;
    }

    /**
     * This method handles the incoming request by creating a {@link Game} object in the database.
     * <p>
     * It then returns the game object.
     * <p>
     * If the usernames are null, this should throw an InvalidRequestException.
     *
     * @param request request object containing the username(s) of the player(s) and the botDifficulty.
     * @return CreateNewGameResponse object containing the {@link Game}.
     */
    public CreateNewGameResponse handleRequest(CreateNewGameRequest request) {
        if (request.getAuthPlayerUsername() == null && request.getOtherPlayerUsername() == null) {
            throw new InvalidRequestException("Both player usernames can not be null");
        }

        String whitePlayerUsername = request.getAuthUserWhite() ?
                request.getAuthPlayerUsername() :
                request.getOtherPlayerUsername();

        String blackPlayerUsername = request.getAuthUserWhite() ?
                request.getOtherPlayerUsername() :
                request.getAuthPlayerUsername();

        Game game = new Game();
        game.setActive("true");
        game.setGameId(ChessUtils.generateGameId());
        game.setBotDifficulty(request.getBotDifficulty());
        game.setNotation(ChessUtils.STARTING_NOTATION);
        game.setValidMoves(ChessUtils.STARTING_VALID_MOVES);
        game.setBlackPlayerUsername(blackPlayerUsername);
        game.setWhitePlayerUsername(whitePlayerUsername);
        game.setIsVisible("true");
        gameDao.save(game);

        if (whitePlayerUsername != null) {
            User user = userDao.load(whitePlayerUsername);
            if (user.getGames() == null) {
                user.setGames(Set.of(game.getGameId()));
            } else {
                user.getGames().add(game.getGameId());
            }
            userDao.saveUser(user);
        }

        if (blackPlayerUsername != null) {
            User user = userDao.load(blackPlayerUsername);
            if (user.getGames() == null) {
                user.setGames(Set.of(game.getGameId()));
            } else {
                user.getGames().add(game.getGameId());
            }
            userDao.saveUser(user);
        }

        return CreateNewGameResponse.builder()
                .withGameId(game.getGameId())
                .build();
    }
}
