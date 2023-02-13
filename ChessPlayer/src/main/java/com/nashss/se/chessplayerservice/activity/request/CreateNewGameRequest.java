package com.nashss.se.chessplayerservice.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = CreateNewGameRequest.Builder.class)
public class CreateNewGameRequest {
    private final boolean authUserWhite;
    private final String authPlayerUsername;
    private final String otherPlayerUsername;
    private final int botDifficulty;

    private CreateNewGameRequest(boolean authUserWhite,
                                 String authPlayerUsername,
                                 String otherPlayerUsername,
                                 int botDifficulty) {
        this.authUserWhite = authUserWhite;
        this.authPlayerUsername = authPlayerUsername;
        this.otherPlayerUsername = otherPlayerUsername;
        this.botDifficulty = botDifficulty;
    }

    public boolean getAuthUserWhite() {
        return authUserWhite;
    }

    public String getAuthPlayerUsername() {
        return authPlayerUsername;
    }

    public String getOtherPlayerUsername() {
        return otherPlayerUsername;
    }

    public int getBotDifficulty() {
        return botDifficulty;
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private boolean authUserWhite;
        private String authPlayerUsername;
        private String otherPlayerUsername;
        private int botDifficulty;

        public Builder withAuthUserWhite(boolean authUserWhite) {
            this.authUserWhite = authUserWhite;
            return this;
        }

        public Builder withAuthPlayerUsername(String authPlayerUsername) {
            this.authPlayerUsername = authPlayerUsername;
            return this;
        }

        public Builder withOtherPlayerUsername(String otherPlayerUsername) {
            this.otherPlayerUsername = otherPlayerUsername;
            return this;
        }

        public Builder withBotDifficulty(int botDifficulty) {
            this.botDifficulty = botDifficulty;
            return this;
        }

        public CreateNewGameRequest build() {
            return new CreateNewGameRequest(authUserWhite, authPlayerUsername, otherPlayerUsername, botDifficulty);
        }
    }
}
