package com.nashss.se.chessplayerservice.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class ChessUtils {

    public static final String STARTING_NOTATION =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final String STARTING_VALID_MOVES =
            "a2a3,b2b3,c2c3,d2d3,e2e3,f2f3,g2g3,h2h3,a2a4,b2b4,c2c4,d2d4,e2e4,f2f4,g2g4,h2h4,b1a3,b1c3,g1f3,g1h3";

    private static final Pattern EMAIL_CHARACTER_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    public enum WINNER {
        WHITE("white"),
        BLACK("black"),
        DRAW("draw");

        private final String enumStr;

        /**
         * Utility enum to convert String.
         * @param enumStr the String conversion of the enum
         */
        WINNER(String enumStr) {
            this.enumStr = enumStr;
        }

        public String getEnumStr() {
            return enumStr;
        }

        /**
         * Reverse lookup method.
         * @param value the String to lookup
         * @return the Enum equivalent of the string
         */
        public static Optional<WINNER> getWinnerByString(String value) {
            return Arrays.stream(WINNER.values())
                    .filter(winner -> winner.enumStr.equals(value))
                    .findFirst();
        }
    }

    /**
     * Static utility method to generate a 10-digit alphanumeric gameId.
     * @return a random 10-digit alphanumeric String
     */
    public static String generateGameId() {
        return RandomStringUtils.random(10, true, true);
    }

    /**
     * Static utility method to convert a bot difficulty to a relative elo rating.
     * @param botDifficulty the botDifficulty
     * @return an ELO rating representing the bot's ELO.
     */
    public static int botDifficultyToRating(int botDifficulty) {
        Map<Integer, Integer> diffToRatingMap = new HashMap<>();
        diffToRatingMap.put(0, 600);
        diffToRatingMap.put(1, 750);
        diffToRatingMap.put(2, 900);
        diffToRatingMap.put(3, 1050);
        diffToRatingMap.put(4, 1200);
        diffToRatingMap.put(5, 1350);
        diffToRatingMap.put(6, 1500);
        diffToRatingMap.put(7, 1650);
        diffToRatingMap.put(8, 1800);
        diffToRatingMap.put(9, 1950);
        diffToRatingMap.put(10, 2100);
        diffToRatingMap.put(11, 2250);
        diffToRatingMap.put(12, 2400);
        diffToRatingMap.put(13, 2550);
        diffToRatingMap.put(14, 2700);
        diffToRatingMap.put(15, 2850);
        diffToRatingMap.put(16, 3000);
        diffToRatingMap.put(17, 3150);
        diffToRatingMap.put(18, 3300);
        diffToRatingMap.put(19, 3450);
        diffToRatingMap.put(20, 3600);

        return diffToRatingMap.get(botDifficulty);
    }

    /**
     * Static utility method to validate an email based on the RFC 5322 standard.
     * @param email the Email to check
     * @return a boolean representing the validity of the string as an email
     */
    public static boolean isValidEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        } else {
            return EMAIL_CHARACTER_PATTERN.matcher(email).find();
        }
    }

    /**
     * Static utility method to calculate the change in elo of the white player.
     * @param whiteRating the rating of the white player
     * @param blackRating the rating of the black player
     * @param winner a String indicating who won. Should be one of ["white", "black", "draw"]
     * @return the difference in rating to be added
     */
    public static double calculateRatingForWhite(int whiteRating, int blackRating, String winner) {
        Optional<WINNER> optionalWINNER = WINNER.getWinnerByString(winner);
        if (optionalWINNER.isEmpty()) {
            throw new RuntimeException("winner must be one of [white, black, draw]");
        }
        WINNER winnerEnum = optionalWINNER.get();
        double expectedScore = 1 / (1 + 10.0 * (blackRating - whiteRating) / 400);
        if (winnerEnum.equals(WINNER.WHITE)) {
            return (1 - expectedScore) * 25;
        } else if (winnerEnum.equals(WINNER.BLACK)) {
            return (1 - expectedScore) * 25 * -1;
        } else {
            return (.5 - expectedScore) * 25;
        }
    }

    /**
     * Static utility method to calculate the change in elo of the black player.
     * @param whiteRating the rating of the white player
     * @param blackRating the rating of the black player
     * @param winner a String indicating who won. Should be one of ["white", "black", "draw"]
     * @return the difference in rating to be added
     */
    public static double calculateRatingForBlack(int whiteRating, int blackRating, String winner) {
        Optional<WINNER> optionalWINNER = WINNER.getWinnerByString(winner);
        if (optionalWINNER.isEmpty()) {
            throw new RuntimeException("winner must be one of [white, black, draw]");
        }
        WINNER winnerEnum = optionalWINNER.get();
        double expectedScore = 1 / (1 + 10.0 * (whiteRating - blackRating) / 400);
        if (winnerEnum.equals(WINNER.WHITE)) {
            return (1 - expectedScore) * 25 * -1;
        } else if (winnerEnum.equals(WINNER.BLACK)) {
            return (1 - expectedScore) * 25;
        } else {
            return (.5 - expectedScore) * 25;
        }
    }
}
