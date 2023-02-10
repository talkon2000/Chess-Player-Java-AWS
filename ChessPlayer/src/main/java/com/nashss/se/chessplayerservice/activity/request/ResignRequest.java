package com.nashss.se.chessplayerservice.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = ResignRequest.Builder.class)
public class ResignRequest {
    private final String gameId;
    private final String username;

    private ResignRequest(String gameId, String username) {
        this.gameId = gameId;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getGameId() {
        return gameId;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String gameId;
        private String username;

        public Builder withGameId(String gameId) {
            this.gameId = gameId;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public ResignRequest build() {
            return new ResignRequest(gameId, username);
        }
    }
}
