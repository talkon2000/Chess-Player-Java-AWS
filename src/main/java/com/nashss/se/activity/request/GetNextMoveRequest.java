package com.nashss.se.activity.request;

public class GetNextMoveRequest {

    private final String move;
    private final String gameId;

    private GetNextMoveRequest(String move, String gameId) {
        this.move = move;
        this.gameId = gameId;
    }

    public String getMove() {
        return move;
    }

    public String getGameId() {
        return gameId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String move;
        private String gameId;

        public Builder withMove(String move) {
            this.move = move;
            return this;
        }

        public Builder withGameId(String gameId) {
            this.gameId = gameId;
            return this;
        }

        public GetNextMoveRequest build() {
            return new GetNextMoveRequest(move, gameId);
        }
    }
}
