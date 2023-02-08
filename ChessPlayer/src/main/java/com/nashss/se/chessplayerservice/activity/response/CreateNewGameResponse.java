package com.nashss.se.chessplayerservice.activity.response;

public class CreateNewGameResponse {
    private final String gameId;

    private CreateNewGameResponse(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String gameId;

        public Builder withGameId(String gameId) {
            this.gameId = gameId;
            return this;
        }

        public CreateNewGameResponse build() {
            return new CreateNewGameResponse(gameId);
        }
    }
}
