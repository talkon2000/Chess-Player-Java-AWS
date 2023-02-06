package com.nashss.se.chessplayerservice.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetGameRequest.Builder.class)
public class GetGameRequest {
    private final String gameId;

    private GetGameRequest(String gameId) {
        this.gameId = gameId;
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

        public Builder withGameId(String gameId) {
            this.gameId = gameId;
            return this;
        }

        public GetGameRequest build() {
            return new GetGameRequest(gameId);
        }
    }
}
