package com.nashss.se.chessplayerservice.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetAllGamesRequest.Builder.class)
public class GetAllGamesRequest {
    private final String username;
    private final boolean returnHiddenGames;

    private GetAllGamesRequest(String username, boolean returnHiddenGames) {
        this.username = username;
        this.returnHiddenGames = returnHiddenGames;
    }

    public String getUsername() {
        return username;
    }

    public boolean getReturnHiddenGames() {
        return returnHiddenGames;
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String username;
        private boolean returnHiddenGames;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withHiddenGames(boolean returnHiddenGames) {
            this.returnHiddenGames = returnHiddenGames;
            return this;
        }

        public GetAllGamesRequest build() {
            return new GetAllGamesRequest(username, returnHiddenGames);
        }

    }
}
