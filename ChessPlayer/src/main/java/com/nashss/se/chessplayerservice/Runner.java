package com.nashss.se.chessplayerservice;

import com.nashss.se.chessplayerservice.engine.Stockfish;

import java.util.List;

public class Runner {

    private static String STARTING_POS = "startpos ";

    public static void main(String[] args) {
        Stockfish stockfish = new Stockfish();

        if (stockfish.startEngine()) {
            System.out.println("good");
        }
        else {
            System.out.println("bad");
        }

        stockfish.sendCommand("uci");
        stockfish.sendCommand("setoption name UCI_LimitStrength value true");
        stockfish.sendCommand("setoption name UCI_Elo value 100");
        stockfish.sendCommand("ucinewgame");
        stockfish.getOutput(10);


        System.out.println(stockfish.getBestMove(STARTING_POS, 1000));

        String newPos = STARTING_POS + " moves " + "d2d3 e7e6 d3d4 f8b4";
        List<String> validMoves = stockfish.getLegalMoves(newPos);

        validMoves.forEach(System.out::println);
        System.out.println(validMoves.size());
        stockfish.drawBoard(newPos);
        System.out.println(stockfish.getEvalScore(newPos, 1000));
        stockfish.stopEngine();
    }
}
