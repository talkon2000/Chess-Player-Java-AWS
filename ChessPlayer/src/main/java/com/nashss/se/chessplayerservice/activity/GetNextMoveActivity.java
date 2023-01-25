package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.Runner;
import com.nashss.se.chessplayerservice.activity.request.GetNextMoveRequest;
import com.nashss.se.chessplayerservice.activity.response.GetNextMoveResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.engine.Stockfish;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import com.nashss.se.chessplayerservice.exceptions.StockfishException;

import javax.inject.Inject;
import java.util.List;

public class GetNextMoveActivity {

    private final GameDao gameDao;
    private final Stockfish stockfish;

    @Inject
    public GetNextMoveActivity(GameDao dao, Stockfish stockfish) {
        this.gameDao = dao;
        this.stockfish = stockfish;
    }

    public GetNextMoveResponse handleRequest(GetNextMoveRequest request) {
        if (request.getMove() == null || request.getGameId() == null) {
            throw new InvalidRequestException(
                    String.format("Missing one or more required fields: move={%s}, gameId={%s}",
                    request.getMove(), request.getGameId()));
        }

        if (!stockfish.startEngine()) {
            throw new StockfishException("Engine failed to start");
        }
        String active = "true";
        Game game = gameDao.load(request.getGameId(), active);
        if (game == null) {
            throw new InvalidRequestException("There is no game with that ID");
        }
        stockfish.sendCommand("uci");
        stockfish.sendCommand("setoption name skill level value " + game.getBotDifficulty());
        stockfish.sendCommand("ucinewgame");
        stockfish.getOutput(10);
        String engineMove = stockfish.getBestMove(game.getMoves() + " " + request.getMove(), 500);
        List<String> validMoves = stockfish.getLegalMoves(game.getMoves());
        stockfish.stopEngine();

        game.setMoves(game.getMoves() + " " + request.getMove() + " " + engineMove);
        gameDao.save(game);
        // TODO: LOGIC FOR GAME OVER
        String winner = null; // VALID VALUES ARE WHITE, BLACK, DRAW, OR NULL

        return GetNextMoveResponse.builder().withMove(engineMove).withValidMoves(validMoves).withWinner(winner).build();
    }
}
