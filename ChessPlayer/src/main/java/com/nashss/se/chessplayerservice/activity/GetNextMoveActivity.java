package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetNextMoveRequest;
import com.nashss.se.chessplayerservice.activity.response.GetNextMoveResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.GameDao;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.engine.Stockfish;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import com.nashss.se.chessplayerservice.exceptions.StockfishException;
import com.nashss.se.chessplayerservice.utils.ChessUtils;

import javax.inject.Inject;
import java.util.*;

public class GetNextMoveActivity {

    private final GameDao gameDao;
    private final UserDao userDao;
    private final Stockfish stockfish;

    @Inject
    public GetNextMoveActivity(GameDao gameDao, UserDao userDao, Stockfish stockfish) {
        this.gameDao = gameDao;
        this.userDao = userDao;
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
        game.setMoves(game.getMoves() == null ? request.getMove() : game.getMoves() + " " + request.getMove());

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
            game.setMoves(game.getMoves() == null ? request.getMove() : game.getMoves() + " " + request.getMove());
            // Check if the engine move ends the game
            gameOverChecker(game);
            StringBuilder sb = new StringBuilder();
            for (String move : stockfish.getLegalMoves(game.getNotation())) {
                sb.append(move);
                sb.append(",");
            }
            game.setValidMoves(sb.toString());
        }

        stockfish.stopEngine();

        // Save the new notation to the database before returning
        gameDao.save(game);

        // If the game is over, edit the user(s) rating scores
        // If expected Score is above .5, you are expected to either win or draw
        if (game.getWinner() != null) {
            String winner = game.getWinner();
            // If multiplayer
            if (game.getBotDifficulty() != null) {
                User white = userDao.load(game.getWhitePlayerUsername());
                User black = userDao.load(game.getBlackPlayerUsername());
                int whiteRating = white.getRating();
                int blackRating = black.getRating();
                double whiteExpectedScore = 1 / (1 + 10.0 * (blackRating - whiteRating) / 400);
                double blackExpectedScore = 1 / (1 + 10.0 * (whiteRating - blackRating) / 400);
                if (winner.equals("white")) {
                    whiteRating += (1 - whiteExpectedScore) * 25;
                    blackRating -= (1 - blackExpectedScore) * 25;
                }
                if (winner.equals("black")) {
                    whiteRating -= (1- whiteExpectedScore) * 25;
                    blackRating -= (1- blackExpectedScore) * 25;
                }
                if (winner.equals("draw")) {
                    whiteRating += (.5 - whiteExpectedScore) * 25;
                    blackRating += (.5 - blackExpectedScore) * 25;
                }
                white.setRating(whiteRating);
                black.setRating(blackRating);
                userDao.saveUser(white);
                userDao.saveUser(black);
            }
            // If white vs bot
            else if (game.getWhitePlayerUsername() != null) {
                User white = userDao.load(game.getWhitePlayerUsername());
                int whiteRating = white.getRating();
                int botRating = ChessUtils.botDifficultyToRating(game.getBotDifficulty());
                double expectedScore = 1 / (1 + 10.0 * (botRating - whiteRating) / 400);
                if (winner.equals("white")) {
                    whiteRating += (1 - expectedScore) * 25;
                }
                if (winner.equals("black")) {
                    whiteRating -= (1- expectedScore) * 25;
                }
                if (winner.equals("draw")) {
                    whiteRating += (.5 - expectedScore) * 25;
                }
                white.setRating(whiteRating);
                userDao.saveUser(white);
            }
            // If black vs bot
            else {
                User black = userDao.load(game.getBlackPlayerUsername());
                int blackRating = black.getRating();
                int botRating = ChessUtils.botDifficultyToRating(game.getBotDifficulty());
                double expectedScore = 1 / (1 + 10.0 * (botRating - blackRating) / 400);
                if (winner.equals("black")) {
                    blackRating += (1- expectedScore) * 25;
                }
                if (winner.equals("white")) {
                    blackRating -= (1 - expectedScore) * 25;
                }
                if (winner.equals("draw")) {
                    blackRating += (.5 - expectedScore) * 25;
                }
                black.setRating(blackRating);
                userDao.saveUser(black);
            }
        }

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
