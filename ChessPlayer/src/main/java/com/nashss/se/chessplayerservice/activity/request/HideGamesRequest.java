package com.nashss.se.chessplayerservice.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = HideGamesRequest.Builder.class)
public class HideGamesRequest {
    private final String[] gameIds;

    private HideGamesRequest(String[] gameIds) {
        this.gameIds = gameIds;
    }

    public String[] getGameIds() {
        return gameIds;
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String[] gameIds;

        public Builder withGameIds(String[] gameIds) {
            this.gameIds = gameIds;
            return this;
        }

        public HideGamesRequest build() {
            return new HideGamesRequest(gameIds);
        }
    }
}
