package com.nashss.se;

import com.nashss.se.engine.Stockfish;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Runner {

    private static String STARTING_POS = "fem rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w";

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
        List<String> legalMoves = Arrays.stream(stockfish.getLegalMoves(newPos).split("\n"))
                .map(s -> s.split(":")[0])
                .filter(s -> !s.isBlank() && !s.contains("Nodes"))
                .peek(System.out::println)
                .collect(Collectors.toList());

        System.out.println(legalMoves.size());
        stockfish.drawBoard(newPos);
        System.out.println(stockfish.getEvalScore(newPos, 1000));
        stockfish.stopEngine();
    }
}
