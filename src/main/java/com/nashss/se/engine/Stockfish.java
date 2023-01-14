package com.nashss.se.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

/**
 * A simple and efficient client to run Stockfish from Java
 *
 * @author Rahul A R and modified by Josh Taylor
 *
 */
public class Stockfish {

    private BufferedReader processReader;
    private OutputStreamWriter processWriter;

    private static final String PATH = "engine/stockfish/stockfish.exe";

    /**
     * Starts Stockfish engine as a process and initializes it.
     * Make sure to run stopEngine() before you are done.
     *
     * @return True on success. False otherwise
     */
    public boolean startEngine() {
        try {
            Process engineProcess = Runtime.getRuntime().exec(PATH);
            processReader = new BufferedReader(new InputStreamReader(
                    engineProcess.getInputStream()));
            processWriter = new OutputStreamWriter(
                    engineProcess.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Stops Stockfish and cleans up before closing it.
     */
    public void stopEngine() {
        try {
            sendCommand("quit");
            processReader.close();
            processWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes in any valid UCI command and executes it.
     * If getOutput() is not used to flush the output reader, you may experience unintended behavior.
     *
     * @param command UCI command to relay to the engine
     */
    public void sendCommand(String command) {
        try {
            processWriter.write(command + "\n");
            processWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is generally called right after 'sendCommand' for getting the raw output from Stockfish.
     *
     * @param waitTime
     *            Time in milliseconds for which the function waits before
     *            reading the output. Useful when a long-running command is
     *            executed.
     * @return Raw output from Stockfish as a String.
     */
    public String getOutput(int waitTime) {
        StringBuilder output = new StringBuilder();
        try {
            Thread.sleep(waitTime);
            sendCommand("isready");
            while (true) {
                String text = processReader.readLine();
                if (text.equals("readyok"))
                    break;
                else
                    output.append(text).append("\n");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    /**
     * This function returns the best move for a given position after
     * calculating for 'waitTime' ms
     *
     * @param position Position string. Valid input is either "startpos moves "
     *                 followed by moves in pure algebraic notation
     *                 without from-to delimiters (example: d2d4)
     *                 or "fem " followed by a position in FEM notation
     * @param waitTime engine calculation time in milliseconds
     * @return Best Move in pure algebraic format. For example: "d2d4"
     */
    public String getBestMove(String position, int waitTime) {
        sendCommand("position " + position);
        sendCommand("go movetime " + waitTime);
        return getOutput(waitTime + 20).split("bestmove ")[1].split("ponder")[0];
    }

    /**
     * Get a list of all legal moves from the given position
     *
     * @param position Position string. Valid input is either
     *                 "startpos moves " followed by moves in pure algebraic notation
     *                 without from-to delimiters (example: d2d4)
     *                 or "fem " followed by a position in FEM notation
     * @return String of moves
     */
    public String getLegalMoves(String position) {
        sendCommand("position " + position);
        sendCommand("go perft 1");
        return getOutput(10);
    }

    /**
     * Draws the current state of the chessboard.
     *
     * @param position Position string. Valid input is either
     *                 "startpos moves " followed by moves in pure algebraic notation
     *                 without from-to delimiters (example: d2d4)
     *                 or "fem " followed by a position in FEM notation
     */
    public void drawBoard(String position) {
        sendCommand("position " + position);
        sendCommand("d");

        String[] rows = getOutput(0).split("\n");

        for (int i = 1; i < 20; i++) {
            System.out.println(rows[i]);
        }
    }

    /**
     * Get the evaluation score of a given board position
     *
     * @param position Position string. Valid input is either
     *                 "startpos moves " followed by moves in pure algebraic notation
     *                 without from-to delimiters (example: d2d4)
     *                 or "fem " followed by a position in FEM notation
     * @param waitTime in milliseconds
     * @return evalScore
     */
    public float getEvalScore(String position, int waitTime) {
        sendCommand("position " + position);
        sendCommand("go movetime " + waitTime);

        String[] dump = getOutput(waitTime + 20).split("\n");
        for (int i = dump.length - 1; i >= 0; i--) {
            if (dump[i].startsWith("info depth ")) {
                return Float.parseFloat(dump[i].split("score cp ")[1].split(" ")[0]) / 100;
            }
        }
        return 0.0f;
    }
}
