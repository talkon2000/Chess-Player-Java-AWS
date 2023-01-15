package com.nashss.se.chessplayerservice.activity;

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
    private final Stockfish stockfish = new Stockfish();

    @Inject
    public GetNextMoveActivity(GameDao dao) {
        this.gameDao = dao;
        //this.stockfish = stockfish;
    }

    public GetNextMoveResponse handleRequest(GetNextMoveRequest request) {
        if (request.getMove() == null || request.getGameId() == null) {
            throw new InvalidRequestException(
                    String.format("Missing one or more required fields: move={%s}, gameId={%s}",
                    request.getMove(), request.getGameId()));
        }

        Game game = gameDao.get(request.getGameId());
/*        if (stockfish.startEngine()) {
            throw new StockfishException("Engine failed to start");
        }*/
        stockfish.startEngine();
        String engineMove = stockfish.getBestMove(game.getMoves(), 500);
        List<String> validMoves = stockfish.getLegalMoves(game.getMoves());

        stockfish.stopEngine();
        return GetNextMoveResponse.builder().withMove(engineMove).withValidMoves(validMoves).build();
    }
}
