package com.nashss.se.chessplayerservice.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class RegexHelper {
    private static final Pattern EMAIL_CHARACTER_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

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
