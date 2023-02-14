package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.ResignRequest;
import com.nashss.se.chessplayerservice.activity.response.ResignResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import com.nashss.se.chessplayerservice.utils.ChessUtils;

import javax.inject.Inject;

/**
 * Implementation of the ResignActivity for the ChessPlayerClient's Resign API.
 *
 * This API allows the user resign their current game.
 */
public class ResignActivity {

    private final GameDao gameDao;
    private final UserDao userDao;

    /**
     * Instantiates a new ResignActivity object.
     *
     * @param gameDao DAO to access the games table.
     * @param userDao DAO to access the users table.
     */
    @Inject
    public ResignActivity(GameDao gameDao, UserDao userDao) {
        this.gameDao = gameDao;
        this.userDao = userDao;
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
        if (!game.getActive().equals("true")) {
            throw new InvalidRequestException("You cannot resign an inactive game");
        }

        User player = userDao.load(request.getUsername());
        if (player == null) {
            throw new InvalidRequestException("A player with that username does not exist");
        }
        User opponent;
        if (game.getBotDifficulty() == null) {
            opponent = userDao.load(game.getBlackPlayerUsername());
        } else {
            opponent = new User();
            opponent.setRating(ChessUtils.botDifficultyToRating(game.getBotDifficulty()));
        }
        int opponentRating = opponent.getRating();

        if (game.getWhitePlayerUsername() != null &&
                game.getWhitePlayerUsername().equals(request.getUsername())) {
            game.setActive("false");
            game.setWinner("black");
        } else if (game.getBlackPlayerUsername() != null &&
                game.getBlackPlayerUsername().equals(request.getUsername())) {
            game.setActive("false");
            game.setWinner("white");
        } else {
            throw new InvalidRequestException("Username must belong to a user playing the game");
        }

        int ratingChange = (int) ChessUtils.calculateRatingForPlayer(player.getRating(),
                opponentRating,
                ChessUtils.WINNER.OPPONENT);
        player.setRating(player.getRating() + ratingChange);
        userDao.saveUser(player);

        if (opponent.getUsername() != null) {
            int opponentRatingChange = (int) ChessUtils.calculateRatingForPlayer(opponentRating,
                    player.getRating(),
                    ChessUtils.WINNER.PLAYER);
            opponent.setRating(opponent.getRating() + opponentRatingChange);
            userDao.saveUser(opponent);
        }
        gameDao.save(game);

        return ResignResponse.builder()
                .withGame(game)
                .build();
    }
}
