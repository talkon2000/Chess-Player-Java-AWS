package com.nashss.se.chessplayerservice.activity.response;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

public class HideGamesResponse {

    private final String[] gameIds;

    private HideGamesResponse(String[] gameIds) {
        this.gameIds = gameIds;
    }

    public String[] getGameIds() {
        return gameIds;
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String[] gameIds;

        public Builder withGameIds(String[] gameIds) {
            this.gameIds = gameIds;
            return this;
        }

        public HideGamesResponse build() {
            return new HideGamesResponse(gameIds);
        }
    }
}
