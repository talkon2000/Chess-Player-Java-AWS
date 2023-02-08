package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.CreateNewGameRequest;
import com.nashss.se.chessplayerservice.activity.response.CreateNewGameResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import com.nashss.se.chessplayerservice.utils.ChessUtils;

import javax.inject.Inject;
import java.util.Set;

public class CreateNewGameActivity {

    private final UserDao userDao;
    private final GameDao gameDao;

    @Inject
    public CreateNewGameActivity(UserDao userDao, GameDao gameDao) {
        this.userDao = userDao;
        this.gameDao = gameDao;
    }

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
        gameDao.save(game);

        if (whitePlayerUsername != null) {
            User user = userDao.load(whitePlayerUsername);
            if (user.getGames() == null) {
                user.setGames(Set.of(game.getGameId()));
            }
            else {
                user.getGames().add(game.getGameId());
            }
            userDao.saveUser(user);
        }

        if (blackPlayerUsername != null) {
            User user = userDao.load(blackPlayerUsername);
            if (user.getGames() == null) {
                user.setGames(Set.of(game.getGameId()));
            }
            else {
                user.getGames().add(game.getGameId());
            }
            userDao.saveUser(user);
        }

        return CreateNewGameResponse.builder()
                .withGameId(game.getGameId())
                .build();
    }
}
