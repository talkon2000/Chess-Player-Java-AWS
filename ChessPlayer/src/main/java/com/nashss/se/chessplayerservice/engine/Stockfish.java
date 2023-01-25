package com.nashss.se.chessplayerservice.engine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * A simple and efficient client to run Stockfish from Java
 *
 * @author Rahul A R and modified by Josh Taylor
 *
 */
public class Stockfish {
    private BufferedReader processReader;
    private OutputStreamWriter processWriter;

    private final String PATH = getEngineLocation();

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
            throw new RuntimeException(e);
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
        String output = getOutput(waitTime + 20);
        return output.split("bestmove ")[1].split(" ponder")[0];
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
    public List<String> getLegalMoves(String position) {
        sendCommand("position " + position);
        sendCommand("go perft 1");
        String output = getOutput(10);
        return Arrays.stream(output.split("\n"))
                .map(s -> s.split(":")[0])
                .filter(s -> !s.isBlank() && !s.contains("Nodes"))
                .collect(Collectors.toList());
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
     * @return evalScore
     */
    public String getEvalScore(String position) {
        sendCommand("position " + position);
        sendCommand("eval");

        String[] dump = getOutput(20).split("\n");
        for (int i = dump.length - 1; i >= 0; i--) {
            if (dump[i].startsWith("Final evaluation")) {
                return dump[i];
            }
        }
        return "";
    }

    private String getEngineLocation() {
        // If running locally
        String path = "engine/stockfish";
        if (new File(path).canExecute()) {
            System.out.println("local");
            return path;
        }

        // If running in Lambda
        path = "/var/task/./lib/stockfish";
        if (new File(path).canExecute()) {
            System.out.println("lambda");
            return path;
        }

        // If running in docker
        String origPath = "/var/task/./lib/stockfish";
        String newPath = "/tmp/stockfish";

        Runtime runtime = Runtime.getRuntime();
        try {
            Files.copy(
                    Path.of(origPath),
                    Path.of(newPath),
                    REPLACE_EXISTING);
            System.out.println("chmod in docker");
            runtime.exec("chmod 755 " + newPath);
        } catch (IOException e) {
            System.out.println("Engine could not be moved");
            throw new RuntimeException(e);
        }

        return newPath;
    }
}
