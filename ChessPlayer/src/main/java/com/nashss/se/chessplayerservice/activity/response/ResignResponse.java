package com.nashss.se.chessplayerservice.activity.response;

import com.nashss.se.chessplayerservice.dynamodb.models.Game;

public class ResignResponse {
    private final Game game;

    private ResignResponse(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Game game;

        public Builder withGame(final Game game) {
            this.game = game;
            return this;
        }

        public ResignResponse build() {
            return new ResignResponse(game);
        }
    }
}
