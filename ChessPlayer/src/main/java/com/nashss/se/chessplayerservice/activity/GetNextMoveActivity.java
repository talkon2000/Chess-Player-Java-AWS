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
import java.util.*;

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

        // Initialize stockfish
        if (!stockfish.startEngine()) {
            throw new StockfishException("Engine failed to start");
        }
        stockfish.getOutput(1);
        // Check if the submitted move is legal
        List<String> legalMoves = stockfish.getLegalMoves("fen " + game.getNotation());
        if (!legalMoves.contains(request.getMove())) {
            throw new InvalidRequestException("That is not a legal move");
        }
        game.setNotation(game.getNotation() + " moves " + request.getMove());

        // Check if the player move ends the game
        // This method also updates the game's moves to be fen notation
        gameOverChecker(game);

        stockfish.sendCommand("uci");
        stockfish.sendCommand("setoption name skill level value " + game.getBotDifficulty());
        stockfish.getOutput(10);
        String engineMove = null;
        List<String> validMoves = null;
        if (game.getWinner() == null) {
            // If the player move did not end the game, make an engine move
            engineMove = stockfish.getBestMove(String.format("fen %s", game.getNotation()), 500).trim();
            game.setNotation(game.getNotation() + " moves " + engineMove);
            // Check if the engine move ends the game
            gameOverChecker(game);
            game.setValidMoves(new HashSet<>(stockfish.getLegalMoves(game.getNotation())));
        }

        stockfish.stopEngine();

        // Save the new notation to the database before returning
        gameDao.save(game);
        return GetNextMoveResponse.builder()
                .withGame(game)
                .withMove(engineMove)
                .build();
    }

    private void gameOverChecker(Game game) {
        List<String> legalMoves = stockfish.getLegalMoves("fen " + game.getNotation());

        // Initialize inCheck to false
        boolean inCheck = false;
        int fiftyMoveRule = 0;
        String pieces = "";
        stockfish.sendCommand("d");
        String[] dump = stockfish.getOutput(10).split("\n");
        for (String line : dump) {
            // Get new simplified notation
            // 5th field of fen string is 50 move rule
            if (line.startsWith("Fen: ")) {
                String fen = line.split("Fen: ")[1];
                game.setNotation(fen);
                pieces = fen.split(" ")[0];
                fiftyMoveRule = Integer.parseInt(fen.split(" ")[4]);
            }
            if (line.startsWith("Checkers: ")) {
                // See if position is in check
                line = line.replace("Checkers: ", "").trim();
                if (!line.isBlank()) {
                    inCheck = true;
                }
            }
        }

        // Logic to determine if enough material is present
        List<Character> pieceList = List.of('p', 'P', 'q', 'Q', 'r', 'R');
        StringBuilder whitePieces = new StringBuilder();
        StringBuilder blackPieces = new StringBuilder();
        boolean enoughMaterial = false;
        // loop to collect all the relevant pieces
        for (Character c : pieces.toCharArray()) {
            // ignore fen notation breaks for rows
            if (c.equals('/') || c.toString().equalsIgnoreCase("k")) {
                continue;
            }
            // if a pawn, queen, or rook is found, there is enough material
            if (pieceList.contains(c)) {
                enoughMaterial = true;
                break;
            }
            if (Character.isLowerCase(c)) {
                blackPieces.append(c);
                if (blackPieces.length() > 1) {
                    enoughMaterial = true;
                    break;
                }
            }
            else {
                whitePieces.append(c);
                if (whitePieces.length() > 1) {
                    enoughMaterial = true;
                    break;
                }
            }
        }

        // if in check and no valid moves, game is over by checkmate
        if (inCheck && legalMoves.isEmpty()) {
            game.setWinner(game.getNotation().split(" ")[1].equals("w") ? "black" : "white");
            game.setActive("false");
        }

        // if no valid moves, but you are not in check, game is over by stalemate
        else if (legalMoves.isEmpty()) {
            game.setWinner("draw");
            game.setActive("false");
        }

        // fifty move rule is a draw condition;
        // if there have been no pawn captures or advances in 50 moves, the game ends in a draw
        // fen string tracks this for us
        else if (fiftyMoveRule >= 100) {
           game.setWinner("draw");
           game.setActive("false");
        }

        // draw by not enough material
        else if (!enoughMaterial) {
            game.setWinner("draw");
            game.setActive("false");
        }
    }
}
