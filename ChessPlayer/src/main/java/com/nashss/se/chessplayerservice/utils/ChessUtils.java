package com.nashss.se.chessplayerservice.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class ChessUtils {

    public static final String STARTING_NOTATION =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final String STARTING_VALID_MOVES =
            "a2a3,b2b3,c2c3,d2d3,e2e3,f2f3,g2g3,h2h3,a2a4,b2b4,c2c4,d2d4,e2e4,f2f4,g2g4,h2h4,b1a3,b1c3,g1f3,g1h3";

    private static final Pattern EMAIL_CHARACTER_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    public static String generateGameId() {
        return RandomStringUtils.random(10, true, true);
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
}
