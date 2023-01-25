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

        // Get the game from the database, then set the new move
        Game game = gameDao.load(request.getGameId());
        if (game == null || game.getActive().equals("false")) {
            throw new InvalidRequestException("There is no game with that ID");
        }
        game.setMoves(game.getMoves() + " moves " + request.getMove());

        // Initialize stockfish
        if (!stockfish.startEngine()) {
            throw new StockfishException("Engine failed to start");
        }
        stockfish.sendCommand("uci");
        stockfish.sendCommand("setoption name skill level value " + game.getBotDifficulty());
        stockfish.getOutput(10);

        // Check if the player move ends the game
        // This method also updates the game's moves to be fen notation
        gameOverChecker(game);

        String engineMove = null;
        List<String> validMoves = null;
        if (game.getWinner() == null) {
            // If the player move did not end the game, make an engine move
            engineMove = stockfish.getBestMove(String.format("fen %s", game.getMoves()), 500).trim();
            game.setMoves(game.getMoves() + " moves " + engineMove);
            // Check if the engine move ends the game
            gameOverChecker(game);
            validMoves = stockfish.getLegalMoves(game.getMoves());
        }

        stockfish.stopEngine();

        // Save the new notation to the database before returning
        gameDao.save(game);
        return GetNextMoveResponse.builder().withMove(engineMove).withValidMoves(validMoves).withWinner(game.getWinner()).build();
    }

    private void gameOverChecker(Game game) {
        // VALID VALUES ARE WHITE, BLACK, DRAW, OR NULL
        String pos = "fen " + game.getMoves();
        List<String> legalMoves = stockfish.getLegalMoves(pos);

        // Initialize inCheck to false
        boolean inCheck = false;
        stockfish.sendCommand("d");
        String[] dump = stockfish.getOutput(10).split("\n");
        for (String line : dump) {
            // Get new simplified notation
            if (line.startsWith("Fen: ")) {
                game.setMoves(line.split("Fen: ")[1]);
            }
            if (line.startsWith("Checkers: ")) {
                // See if position is in check
                line = line.replace("Checkers: ", "").trim();
                if (!line.isBlank()) {
                    inCheck = true;
                }
            }
        }

        // if in check and no valid moves, game is over by checkmate
        if (inCheck && legalMoves.isEmpty()) {
            game.setWinner(game.getMoves().split(" ")[1].equals("w") ? "black" : "white");
            game.setActive("false");
        }

        // if no valid moves, but you are not in check, game is over by stalemate
        else if (legalMoves.isEmpty()) {
            game.setWinner("draw");
            game.setActive("false");
        }
        // TODO: add other drawing conditions like not enough material and 50 move rule
    }
}
