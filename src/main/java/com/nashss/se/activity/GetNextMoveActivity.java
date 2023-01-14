package com.nashss.se.activity;

import com.nashss.se.activity.request.GetNextMoveRequest;
import com.nashss.se.activity.response.GetNextMoveResponse;
import com.nashss.se.dynamodb.dao.GameDao;
import com.nashss.se.dynamodb.models.Game;
import com.nashss.se.engine.Stockfish;
import com.nashss.se.exceptions.InvalidRequestException;

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

        Game game = gameDao.get(request.getGameId());
        String engineMove = stockfish.getBestMove(game.getMoves(), 500);
        List<String> validMoves = stockfish.getLegalMoves(game.getMoves());

        return GetNextMoveResponse.builder().withMove(engineMove).withValidMoves(validMoves).build();
    }
}
