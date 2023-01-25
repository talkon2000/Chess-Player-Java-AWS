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


        System.out.println(stockfish.getBestMove(STARTING_POS, 1000) + "\n");

        String newPos = STARTING_POS + " moves " + "d2d3 e7e6 d3d4 f8b4";
        List<String> validMoves = stockfish.getLegalMoves(newPos);

        validMoves.forEach(System.out::println);
        System.out.println(validMoves.size());
        stockfish.drawBoard("fen 8/8/8/8/8/8/1r6/kr5K w - - 1 1");
        System.out.println(stockfish.getEvalScore(newPos));

        System.out.println(stockfish.getLegalMoves("fen 8/8/8/8/8/8/1r6/k1r4K w - - 1 1").isEmpty());
        stockfish.sendCommand("position fen 8/8/8/8/8/8/1rr5/k6K b - - 1 1");
        stockfish.sendCommand("d");
        String[] dump = stockfish.getOutput(10).split("\n");
        for (String line : dump) {
            if (line.startsWith("Checkers: ")) {
                System.out.println(line);
            }
        }
        stockfish.stopEngine();
    }
}
