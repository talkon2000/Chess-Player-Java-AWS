package com.nashss.se.chessplayerservice.activity.response;

import com.nashss.se.chessplayerservice.dynamodb.models.Game;

public class GetGameResponse {
    private final Game game;

    private GetGameResponse(Game game) {
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

        public Builder withGame(Game game) {
            this.game = game;
            return this;
        }

        public GetGameResponse build() {
            return new GetGameResponse(game);
        }
    }
}
