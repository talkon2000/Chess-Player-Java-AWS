package com.nashss.se.chessplayerservice.activity.response;

import com.nashss.se.chessplayerservice.dynamodb.models.Game;

public class GetNextMoveResponse {
    private final Game game;
    private final String move;

    private GetNextMoveResponse(Game game, String move) {
        this.game = game;
        this.move = move;
    }

    public Game getGame() {
        return game;
    }

    public String getMove() {
        return move;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Game game;
        private String move;

        public Builder withGame(final Game game) {
            this.game = game;
            return this;
        }

        public Builder withMove(final String move) {
            this.move = move;
            return this;
        }

        public GetNextMoveResponse build() {
            return new GetNextMoveResponse(game, move);
        }
    }
}
